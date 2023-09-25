package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.dto.response.LoginResponseDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody
                                             UserRequestDTO requestDTO) throws SocialAppException, Exception{
        UserResponseDTO responseDTO = userService.register(requestDTO);
        return ResponseEntity.ok()
                .body(Response.success(Constants.RESPONSE_CODE.SUCCESS, "User registered successfully!!!")
                        .withData(responseDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequestDTO request) throws SocialAppException{
        try {
            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .otp(otpService.sendOTP(request))
                    .build();
            return ResponseEntity.ok(Response.success(Constants.RESPONSE_CODE.SUCCESS, "The OTP is only valid for 30s")
                    .withData(responseDTO.getOtp()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(Constants.RESPONSE_CODE.BAD_REQUEST, "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(Constants.RESPONSE_CODE.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Response> sendOtp(@RequestBody VerifyRequestDTO requestDTO) throws SocialAppException{
        return ResponseEntity.ok()
                .body(otpService.verifyOtp(requestDTO));
    }
}
