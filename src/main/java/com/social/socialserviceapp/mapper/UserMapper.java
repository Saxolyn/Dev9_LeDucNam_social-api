package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;

    public User convertRequestDTOToUser(UserRequestDTO requestDTO){
        return modelMapper.map(requestDTO, User.class);
    }

    public UserResponseDTO convertUserToUserResponseDTO(User user){
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
