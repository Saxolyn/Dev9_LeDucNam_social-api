package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.dto.request.ForgotPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.ResetPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public interface UserService {

    public Response register(UserRequestDTO requestDTO);

    public Response forgotPassword(ForgotPasswordRequestDTO requestDTO);

    public Response resetPassword(String token, ResetPasswordRequestDTO requestDTO);

    public Optional<User> findUserByUsername(String username);

}
