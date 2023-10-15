package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.exception.MappingException;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileMapperTest {

    @InjectMocks
    private ProfileMapper profileMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @Mock
    private ProfileRepository profileRepository;

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
    void convertRequestDTOToProfile(){
        UpdateInformationRequestDTO requestDTO = UpdateInformationRequestDTO.builder()
                .birthDate("2002-05-07")
                .livePlace("aduchat")
                .occupation("dsadasd")
                .realName("trebtretb")
                .build();
        when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(new Profile());
        profileMapper.convertRequestDTOToProfile(requestDTO);
    }

    @Test
    void convertRequestDTOToProfile_ifProfileNull(){
        UpdateInformationRequestDTO requestDTO = UpdateInformationRequestDTO.builder()
                .birthDate("2002-05-07")
                .livePlace("aduchat")
                .occupation("dsadasd")
                .realName("trebtretb")
                .build();
        when(profileRepository.findProfileByLastModifiedBy(anyString())).thenReturn(null);
        profileMapper.convertRequestDTOToProfile(requestDTO);
    }

    @Test
    void convertRequestDTOToProfile_throwMappingException(){
        try {
            UpdateInformationRequestDTO requestDTO = UpdateInformationRequestDTO.builder()
                    .birthDate("2002-05-07")
                    .livePlace("aduchat")
                    .occupation("dsadasd")
                    .realName("trebtretb")
                    .build();
            when(profileRepository.findProfileByLastModifiedBy(anyString())).thenThrow(MappingException.class);
            profileMapper.convertRequestDTOToProfile(requestDTO);
        } catch (MappingException e) {
            Assertions.assertEquals(Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR, e.getMessage());
        }
    }

    @Test
    void covertProfileToMyInfoResponseDTO(){

        Profile profile = new Profile();
        MyInfoResponseDTO expectedDto = new MyInfoResponseDTO();
        MyInfoResponseDTO result = profileMapper.covertProfileToMyInfoResponseDTO(profile);
        Assertions.assertEquals(expectedDto, result);
    }

    @Test
    void covertProfileToMyInfoResponseDTO_throwMappingException(){
        MappingException mappingException = assertThrows(MappingException.class,
                () -> profileMapper.covertProfileToMyInfoResponseDTO(null));
        assertTrue(mappingException.getMessage()
                .contains(Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR));
    }
}