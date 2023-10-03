package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.ExpiredOtpException;
import com.social.socialserviceapp.exception.InvalidOtpException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.dto.response.LoginResponseDTO;
import com.social.socialserviceapp.model.dto.response.VerifyResponseDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.security.JwtTokenProvider;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
    private static final String DEFAULT_ALGORITHM = "SHA1PRNG";
    private RedisUtil redisUtil;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @Autowired
    public OtpServiceImpl(RedisUtil redisUtil, AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.redisUtil = redisUtil;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public int generateOTP() throws SocialAppException {
        int otp = 0;
        try {
            Random random = SecureRandom.getInstance(DEFAULT_ALGORITHM);
            otp = 100000 + random.nextInt(900000);
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid algorithm", e);
//            throw new SocialAppException("Failed to generate Otp ", e.getMessage());
        }
        return otp;
    }

    @Override
    public Response sendOTP(LoginRequestDTO requestDTO) throws SocialAppException {
        try {
            int otp = this.generateOTP();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            if (authentication.isAuthenticated()) {
                log.info("Otp: {}", otp);
                redisUtil.setValue(requestDTO.getUsername(), otp);
                return Response.success("The OTP is only valid for 30s")
                        .withData(LoginResponseDTO.builder()
                                .otp(otp)
                                .build());
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return null;
    }

    @Override
    public Response verifyOtp(VerifyRequestDTO requestDTO) throws SocialAppException {
        Optional<String> cachedOtp = redisUtil.getValue(requestDTO.getUsername());
        if (cachedOtp.isPresent() && cachedOtp.get()
                .equals(requestDTO.getOtp())) {
            log.info("Otp has found and remove {}", cachedOtp.get());
            redisUtil.remove(requestDTO.getUsername());
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                    requestDTO.getUsername());
            log.info("Logged in User returned [API]: {}", customUserDetails.getUsername());
            String token = jwtTokenProvider.createToken(customUserDetails);
            VerifyResponseDTO responseDTO = new VerifyResponseDTO();
            responseDTO.setUsername(requestDTO.getUsername());
            responseDTO.setToken(token);
            return Response.success()
                    .withData(responseDTO);
        } else if (cachedOtp.isPresent() && !cachedOtp.get()
                .equals(requestDTO.getOtp())) {
            log.error(Constants.RESPONSE_MESSAGE.OTP_EXPIRED);
            throw new ExpiredOtpException(Constants.RESPONSE_MESSAGE.OTP_EXPIRED);
        } else {
            log.error(Constants.RESPONSE_MESSAGE.INVALID_OTP);
            throw new InvalidOtpException(Constants.RESPONSE_MESSAGE.INVALID_OTP.concat("!!!"));
        }
    }
}
