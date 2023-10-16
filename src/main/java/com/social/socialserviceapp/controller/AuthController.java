package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.*;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Auth", description = "The Auth API. Nothing more!!!.")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    @PostMapping("/signup")
    @Operation(summary = "Sign up for a new user account.")
    public Response register(@Valid @RequestBody UserRequestDTO requestDTO){
        return userService.register(requestDTO);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Send an OTP to the response after the user logs in.")
    public Response login(@RequestBody LoginRequestDTO requestDTO){
        return otpService.sendOTP(requestDTO);
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify the OTP after the user provides it.")
    public Response sendOtp(@RequestBody VerifyRequestDTO requestDTO){
        return otpService.verifyOtp(requestDTO);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send a reset password token to the user.")
    public Response forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO){
        return userService.forgotPassword(requestDTO);
    }

    @PutMapping("/reset-password/{token}")
    @Operation(summary = "Reset password with a token, when the user loses their memory.")
    public Response resetPassword(@PathVariable String token,
                                  @Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        return userService.resetPassword(token, resetPasswordRequestDTO);
    }

}
