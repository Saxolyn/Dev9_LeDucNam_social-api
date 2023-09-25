package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UserResponseDTO register(UserRequestDTO requestDTO) throws SocialAppException, Exception;
}
