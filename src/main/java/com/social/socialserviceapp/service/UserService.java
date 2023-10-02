package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.dto.request.ForgotPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.ResetPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public Response register(UserRequestDTO requestDTO);

    public Response forgotPassword(ForgotPasswordRequestDTO requestDTO);

    public Response resetPassword(ResetPasswordRequestDTO requestDTO);

}
