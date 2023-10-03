package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.UserMapper;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.dto.request.ForgotPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.ResetPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.ForgotPasswordResponseDTO;
import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.security.JwtTokenProvider;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserDetailsService userDetailsService,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
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
        return Response.success("User registered successfully!!!").withData(userMapper.convertUserToUserResponseDTO(user));
    }

    @Override
    public Response forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(requestDTO.getUsername());
        return Response.success().withData(ForgotPasswordResponseDTO.builder().token(jwtTokenProvider.createToken(customUserDetails)).build());
    }

    @Override
    public Response resetPassword(ResetPasswordRequestDTO requestDTO) {
        String username = jwtTokenProvider.getUsernameFromJWT(requestDTO.getToken());
        try {
            Optional<User> user = this.findUserByUsername(username);
            user.ifPresent(u -> {
                u.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                userRepository.save(u);

            });
            return Response.success(Constants.RESPONSE_MESSAGE.RESET_PASSWORD_SUCCESS);
        } catch (NotFoundException ex) {
            throw new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND + ": " + username);
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

}
