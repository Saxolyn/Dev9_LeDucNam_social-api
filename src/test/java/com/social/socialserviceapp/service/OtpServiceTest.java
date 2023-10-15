package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.ExpiredOtpException;
import com.social.socialserviceapp.exception.InvalidOtpException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.security.JwtTokenProvider;
import com.social.socialserviceapp.service.impl.OtpServiceImpl;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.RedisUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @InjectMocks
    private OtpServiceImpl otpService;
    @Mock
    private RedisUtil redisUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void generateOTP(){
        OtpServiceImpl.setDefaultAlgorithm("SHA1PRNG");
        otpService.generateOTP();
    }

    @Test
    void generateOTP_invalidAlgorithm(){
        try {
            OtpServiceImpl.setDefaultAlgorithm("323123123123");
            otpService.generateOTP();
        } catch (SocialAppException e) {
            assertEquals("323123123123 SecureRandom not available", e.getMessage());
        }

    }

    @Test
    void sendOTP(){
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken("aduchat", "aduchat",
                updatedAuthorities);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        doNothing().when(redisUtil)
                .setValue(anyString(), anyInt());
        otpService.sendOTP(LoginRequestDTO.builder()
                .username("aduchat")
                .build());
    }

    @Test
    void sendOTP_whenUsernameOrPasswordInvalid(){
        try {
            List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
            Authentication authentication = new UsernamePasswordAuthenticationToken("aduchat", "aduchat",
                    updatedAuthorities);
            when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
            otpService.sendOTP(LoginRequestDTO.builder()
                    .username("aduchat")
                    .build());
        } catch (BadCredentialsException e) {
            Assertions.assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(any()));
        }
    }

    @Test
    void verifyOtp(){
        when(redisUtil.getValue(anyString())).thenReturn(Optional.of("32323"));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new CustomUserDetails(new User()));
        when(jwtTokenProvider.createToken(any())).thenReturn("wqevqwevqwevqwvewv");
        otpService.verifyOtp(VerifyRequestDTO.builder()
                .otp("32323")
                .username("aduchat")
                .build());
    }

    @Test
    void verifyOtp_throwExpiredOtpException(){
        try {
            when(redisUtil.getValue(anyString())).thenReturn(Optional.of("323233"));
            otpService.verifyOtp(VerifyRequestDTO.builder()
                    .otp("32323")
                    .username("aduchat")
                    .build());
        } catch (ExpiredOtpException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.OTP_EXPIRED, e.getMessage());
        }
    }

    @Test
    void verifyOtp_throwInvalidOtpException(){
        try {
            when(redisUtil.getValue(anyString())).thenReturn(Optional.empty());
            otpService.verifyOtp(VerifyRequestDTO.builder()
                    .otp("32323")
                    .username("aduchat")
                    .build());
        } catch (InvalidOtpException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.INVALID_OTP.concat("!!!"), e.getMessage());
        }
    }
}