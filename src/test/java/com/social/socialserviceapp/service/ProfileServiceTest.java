package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.mapper.ProfileMapper;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.ProfileServiceImpl;
import com.social.socialserviceapp.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    ProfileServiceImpl profileService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        User user = new User();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void updateInformation(){
        UpdateInformationRequestDTO requestDTO = UpdateInformationRequestDTO.builder()
                .build();
        when(profileMapper.convertRequestDTOToProfile(any())).thenReturn(new Profile());
        profileService.updateInformation(requestDTO);
    }

    @Test
    void showAvatar(){
        when(profileRepository.findProfileByLastModifiedBy(any())).thenReturn(Profile.builder()
                .avatar("aduchat")
                .build());
        profileService.showAvatar();
    }

    @Test
    void showAvatar_ifProfileNull(){
        try {
            when(profileRepository.findProfileByLastModifiedBy(any())).thenReturn(null);
            profileService.showAvatar();
        } catch (NotFoundException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void showAvatar_ifAvatarNotFound(){
        when(profileRepository.findProfileByLastModifiedBy(any())).thenReturn(new Profile());
        profileService.showAvatar();
    }

    @Test
    void showMyInfo(){
        when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(new Profile());
        when(profileMapper.covertProfileToMyInfoResponseDTO(any())).thenReturn(new MyInfoResponseDTO());
        profileService.showMyInfo();
    }

    @Test
    void showMyInfo_ifProfileNotFound(){
        try {
            when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(null);
            profileService.showMyInfo();
        } catch (NotFoundException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void showOtherInfo_whenUserNotFound(){
        try {
            when(userRepository.findById(anyLong())).thenThrow(NotFoundException.class);
            profileService.showOtherInfo(22L);
        } catch (NotFoundException e) {
            assertThrows(NotFoundException.class, () -> userRepository.findById(anyLong()));
        }
    }

    @Test
    void showOtherInfo_ifProfileNotFound(){
        try {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                    .username("kyrios")
                    .build()));
            when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(null);
            profileService.showOtherInfo(22L);
        } catch (NotFoundException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void showOtherInfo(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .username("kyrios")
                .build()));
        when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(new Profile());
        when(profileMapper.covertProfileToMyInfoResponseDTO(any())).thenReturn(new MyInfoResponseDTO());
        Response response = profileService.showOtherInfo(22L);
        assertNotNull(response.getData());
    }

    @Test
    void uploadAvatar_ifProfileIsNull() throws IOException{
        byte[] imageBytes = new byte[]{};
        MultipartFile multipartFile = new MockMultipartFile("avatar.jpg", "avatar.jpg", "image/jpeg", imageBytes);
        when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(null);
        profileService.uploadAvatar(multipartFile);
    }
}