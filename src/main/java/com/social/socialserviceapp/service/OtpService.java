package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.LoginRequestDTO;
import com.social.socialserviceapp.model.dto.request.VerifyRequestDTO;
import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;

@Service
public interface OtpService {

    public int generateOTP() throws SocialAppException;

    public Response sendOTP(LoginRequestDTO requestDTO) throws SocialAppException;

    public Response verifyOtp(VerifyRequestDTO requestDTO) throws SocialAppException;
}
