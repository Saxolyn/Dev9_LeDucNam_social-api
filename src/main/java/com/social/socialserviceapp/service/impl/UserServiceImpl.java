package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.InvalidTokenRequestException;
import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.UserMapper;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.dto.request.ForgotPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.ResetPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.ForgotPasswordResponseDTO;
import com.social.socialserviceapp.model.entities.PasswordResetToken;
import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.PasswordResetTokenRepository;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.security.JwtTokenProvider;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private RedisUtil redisUtil;
    @Value("${token.password.reset.duration}")
    private Long expiration;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserDetailsService userDetailsService,
                           JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public Response register(UserRequestDTO requestDTO) {
        User user = userMapper.convertRequestDTOToUser(requestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRoleName(RoleName.ROLE_USER).orElse(null);
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return Response.success("User registered successfully!!!")
                .withData(userMapper.convertUserToUserResponseDTO(user));
    }

    @Override
    public Response forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(requestDTO.getUsername());
        PasswordResetToken passwordResetToken = null;
        String token = null;
        if (customUserDetails != null) {
            passwordResetToken = createToken(customUserDetails.getId());
            token = passwordResetToken.getToken();
            if (CommonUtil.isNullOrEmpty(token)) {
                throw new NullPointerException("Plz contact administrator.");
            }
        }
        return Response.success("This token only valid for 1h.")
                .withData(ForgotPasswordResponseDTO.builder().token(token).build());
    }

    public PasswordResetToken createToken(Long userId) {
        passwordResetTokenRepository.deleteByUserId(userId);
        PasswordResetToken token = createPasswordResetForPassword(userId);
        return passwordResetTokenRepository.save(token);
    }

    @Override
    public Response resetPassword(String token, ResetPasswordRequestDTO requestDTO) {
        PasswordResetToken passwordResetToken = validPasswordResetToken(token);
        try {
            Optional<User> user = userRepository.findById(passwordResetToken.getUserId());
            user.ifPresent(u -> {
                u.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                passwordResetToken.setClaimed(true);
                userRepository.save(u);
                passwordResetTokenRepository.save(passwordResetToken);
            });
            return Response.success(Constants.RESPONSE_MESSAGE.RESET_PASSWORD_SUCCESS);
        } catch (NotFoundException ex) {
            throw new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void validate(UserRequestDTO requestDTO) throws SocialAppException {
        List<User> userList = userRepository.findConflictByEmail(requestDTO.getEmail());
        if (!userList.isEmpty()) {
//            throw new SocialAppException(Constants.RESPONSE_CODE.SUCCESS, "Error: Email is already in use!!!");
        }
    }

    public PasswordResetToken createPasswordResetForPassword(Long userId) {
        String tokenID = CommonUtil.generateRandomUuid();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenID);
        token.setExpiryDate(Instant.now().plusMillis(expiration));
        token.setClaimed(false);
        token.setUserId(userId);
        return token;
    }

    public PasswordResetToken validPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByTokenAndClaimed(token, false);
        if (passwordResetToken == null) {
            throw new InvalidTokenRequestException("Password Reset Token", token, "Invalid password reset token");
        } else {
            Instant instant = Instant.now();
            if (passwordResetToken.getExpiryDate().isBefore(instant)) {
                throw new InvalidTokenRequestException("Password Reset Token", token, "Expired password reset token");
            }
        }
        return passwordResetToken;
    }
}
