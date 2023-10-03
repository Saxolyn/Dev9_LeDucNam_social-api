package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.*;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Auth", description = "The Auth API. Nothing more!!!.")
public class AuthController {

    private UserService userService;
    private OtpService otpService;

    @Autowired
    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public Response register(@Valid @RequestBody UserRequestDTO requestDTO) {
        return userService.register(requestDTO);
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequestDTO requestDTO) {
        return otpService.sendOTP(requestDTO);
    }

    @PostMapping("/verify-otp")
    public Response sendOtp(@RequestBody VerifyRequestDTO requestDTO) {
        return otpService.verifyOtp(requestDTO);
    }

    @PostMapping("/forgot-password")
    public Response forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        return userService.forgotPassword(requestDTO);
    }

    @PutMapping("/reset-password")
    public Response resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        return userService.resetPassword(resetPasswordRequestDTO);
    }

}
