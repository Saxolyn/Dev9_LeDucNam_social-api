package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.dto.response.VerifyResponseDTO;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.JwtUtil;
import com.social.socialserviceapp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
    private static final String DEFAULT_ALGORITHM = "SHA1PRNG";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public int generateOTP() throws SocialAppException{
        int otp = 0;
        try {
            Random random = SecureRandom.getInstance(DEFAULT_ALGORITHM);
            otp = 100000 + random.nextInt(900000);
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid algorithm", e);
            throw new SocialAppException("Failed to generate Otp ", e.getMessage());
        }
        return otp;
    }

    @Override
    public int sendOTP(LoginRequestDTO requestDTO) throws SocialAppException{
        int otp = this.generateOTP();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            log.info("Otp: {}", otp);
            redisUtil.setValue(requestDTO.getUsername(), otp);
        }
        return otp;
    }

    @Override
    public Response verifyOtp(VerifyRequestDTO requestDTO) throws SocialAppException{
        Optional<String> cachedUsername = redisUtil.getValue(requestDTO.getUsername());
        if (cachedUsername.isPresent() && cachedUsername.get()
                .equals(requestDTO.getOtp())) {
            log.info("Otp has found and remove", cachedUsername.get());
            redisUtil.remove(requestDTO.getUsername());
            User user = User.builder()
                    .username(requestDTO.getUsername())
                    .password("")
                    .email("")
                    .build();
            String token = jwtUtil.createToken(user);
            VerifyResponseDTO responseDTO = new VerifyResponseDTO();
            responseDTO.setUsername(requestDTO.getUsername());
            responseDTO.setToken(token);
            return Response.success(Constants.RESPONSE_CODE.SUCCESS, "The token will expired 1 hour")
                    .withData(responseDTO);
        } else if (cachedUsername.isPresent() && !cachedUsername.get()
                .equals(requestDTO.getOtp())) {
            throw new SocialAppException(HttpStatus.BAD_REQUEST.name(), "Otp expired!");
        } else {
            throw new SocialAppException(HttpStatus.BAD_REQUEST.name(), "Invalid otp!!!");
        }
    }
}
