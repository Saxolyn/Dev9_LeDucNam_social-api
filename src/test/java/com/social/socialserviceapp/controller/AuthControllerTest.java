package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.*;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private UserService userService;
    @Mock
    private OtpService otpService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void register() {
        when(userService.register(any())).thenReturn(Response.success(Constants.RESPONSE_MESSAGE.SIGNUP_SUCCESSFULLY)
                .withData(new UserRequestDTO()));
        Response response = authController.register(new UserRequestDTO());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void login() {
        when(otpService.sendOTP(any())).thenReturn(Response.success(Constants.RESPONSE_MESSAGE.OTP_VALID_30S));
        Response response = authController.login(new LoginRequestDTO());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void sendOtp() {
        when(otpService.verifyOtp(any())).thenReturn(Response.success()
                .withData(new VerifyRequestDTO()));
        Response response = authController.sendOtp(new VerifyRequestDTO());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void forgotPassword() {
        when(userService.forgotPassword(any())).thenReturn(Response.success(Constants.RESPONSE_MESSAGE.TOKEN_VALID_1H)
                .withData(new ForgotPasswordRequestDTO()));
        Response response = authController.forgotPassword(new ForgotPasswordRequestDTO());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void resetPassword() {
        when(userService.resetPassword(anyString(), any())).thenReturn(
                Response.success(Constants.RESPONSE_MESSAGE.RESET_PASSWORD_SUCCESS));
        Response response = authController.resetPassword("123", new ResetPasswordRequestDTO());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @AfterEach
    void tearDown() {
    }

}