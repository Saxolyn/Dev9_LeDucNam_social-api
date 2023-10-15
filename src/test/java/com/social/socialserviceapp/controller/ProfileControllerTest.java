package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @BeforeEach
    void setUp(){
    }

    @Test
    void updateInformation(){
        UpdateInformationRequestDTO requestDTO = UpdateInformationRequestDTO.builder()
                .build();
        when(profileService.updateInformation(any())).thenReturn(new Response());
        profileController.updateInformation(requestDTO);
    }

    @Test
    void showAvatar() throws IOException{
        when(profileService.showAvatar()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        profileController.showAvatar();
    }

    @Test
    void myInfo(){
        when(profileService.showMyInfo()).thenReturn(new Response());
        profileController.myInfo();
    }

    @Test
    void showOtherInfo(){
        when(profileService.showOtherInfo(anyLong())).thenReturn(new Response());
        profileController.showOtherInfo(11L);
    }

    @Test
    void testUpdateInformation() throws IOException{
        when(profileService.uploadAvatar(any())).thenReturn(new Response());
        profileController.uploadAvatar(null);
    }
}