package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void convertRequestDTOToUser(){
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .email("dsdasd")
                .username("ussarvewr")
                .password("sdtretb")
                .build();
        when(modelMapper.map(requestDTO, User.class)).thenReturn(new User());
        userMapper.convertRequestDTOToUser(requestDTO);
    }

    @Test
    void convertUserToUserResponseDTO(){
        User user = User.builder()
                .email("dsdasd")
                .username("ussarvewr")
                .password("sdtretb")
                .build();
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(new UserResponseDTO());
        userMapper.convertUserToUserResponseDTO(user);
    }
}