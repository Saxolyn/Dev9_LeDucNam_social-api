package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.dto.response.LoginResponseDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.dto.response.VerifyResponseDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.security.JwtTokenProvider;
import com.social.socialserviceapp.service.OtpService;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.CommonUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody
                                             UserRequestDTO requestDTO) throws SocialAppException, Exception{
        UserResponseDTO responseDTO = userService.register(requestDTO);
        return ResponseEntity.ok()
                .body(Response.success("User registered successfully!!!")
                        .withData(responseDTO));
    }

    @PostMapping(value = "/login")
    public @ResponseBody Response login(@RequestBody LoginRequestDTO request){
        try {
            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .otp(otpService.sendOTP(request))
                    .build();
            return Response.success("The OTP is only valid for 30s")
                    .withData(responseDTO.getOtp());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/verify-otp")
    public Response<VerifyResponseDTO> sendOtp(@RequestBody VerifyRequestDTO requestDTO) throws SocialAppException{
        return CommonUtil.getSuccessResult(otpService.verifyOtp(requestDTO));
    }
}
