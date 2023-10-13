package com.social.socialserviceapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.socialserviceapp.mapper.UserMapper;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.dto.request.ForgotPasswordRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.entities.PasswordResetToken;
import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.PasswordResetTokenRepository;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.UserServiceImpl;
import com.social.socialserviceapp.util.CommonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register() throws JSONException, JsonProcessingException {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("leducnam")
                .email("leducnam123@gmail.com")
                .password("leducnam123")
                .build();
        when(userMapper.convertRequestDTOToUser(any())).thenReturn(User.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .build());
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.of(new Role()));
        when(userMapper.convertUserToUserResponseDTO(any())).thenReturn(UserResponseDTO.builder()
                .id(99L)
                .username("leducnam")
                .email("leducnam123@gmail.com")
                .build());
        Response response = userService.register(requestDTO);
        String expectedJson = "{\"id\":99,\"username\":\"leducnam\",\"email\":\"leducnam123@gmail.com\"}";
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();
        JSONAssert.assertEquals(expectedJson, CommonUtil.convertObjectToJson(response.getData()), false);
    }

    @Test
    void forgotPassword() {
        User user = User.builder()
                .username("leducnam")
                .id(99L)
                .build();
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new CustomUserDetails(user));
        when(userService.createToken(anyLong())).thenReturn(new PasswordResetToken());
        when(Instant.now().plusMillis(anyLong())).thenReturn(Instant.ofEpochSecond(32323));
        ForgotPasswordRequestDTO requestDTO = ForgotPasswordRequestDTO.builder()
                .username("leducnam")
                .build();
        Response response = userService.forgotPassword(requestDTO);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void resetPassword() {
    }

    @Test
    void findUserByUsername() {
    }
}