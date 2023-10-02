package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.model.dto.response.LoginResponseDTO;
import com.social.socialserviceapp.model.dto.response.VerifyResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface OtpService {

    public int generateOTP() throws SocialAppException;

    public LoginResponseDTO sendOTP(LoginRequestDTO requestDTO) throws SocialAppException;

    public VerifyResponseDTO verifyOtp(VerifyRequestDTO requestDTO) throws SocialAppException;
}
