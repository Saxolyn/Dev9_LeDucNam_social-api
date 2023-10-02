package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Auth", description = "The Auth API. Nothing more!!!.")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public Response register(@Valid @RequestBody
                             UserRequestDTO requestDTO) throws Exception {
        return Response.success("User registered successfully!!!")
                .withData(userService.register(requestDTO));
    }

    @PostMapping(value = "/login")
    public Response login(@RequestBody LoginRequestDTO requestDTO) {
        return Response.success("The OTP is only valid for 30s")
                .withData(otpService.sendOTP(requestDTO));
    }

    @PostMapping("/verify-otp")
    public Response sendOtp(@RequestBody VerifyRequestDTO requestDTO) {
        return Response.success(null).withData(otpService.verifyOtp(requestDTO));
    }
}
