package com.social.socialserviceapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.mapper.ProfileMapper;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.social.socialserviceapp.util.FileUploadUtil.handleImageUpload;

@Transactional
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Response updateInformation(UpdateInformationRequestDTO requestDTO,
                                      MultipartFile multipartFile) throws IOException{
        Profile profile = profileMapper.convertRequestDTOToProfile(requestDTO);
        profile.setAvatar(handleImageUpload(multipartFile));
        profileRepository.save(profile);
        return Response.success("Updated information successfully");
    }

    @Override
    public ResponseEntity showAvatar() throws IOException{
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByLastModifiedBy(username);
        if (profile == null) {
            throw new NotFoundException("Profile not found.");
        } else {
            try {
                Path folder = Paths.get("src/main/resources/file_upload");
                File fi = new File(folder.toAbsolutePath() + "/" + profile.getAvatar());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(Files.readAllBytes(fi.toPath()));
            } catch (Exception ex) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Response.success("Where did ur avatar go?"));
            }

//            } else {
//                return ResponseEntity.ok()
//                        .body(null);
//            }
        }
    }

    @Override
    public Response showMyInfo(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByLastModifiedBy(username);
        if (profile == null) {
            throw new NotFoundException("Profile not found.");
        } else {
            MyInfoResponseDTO responseDTO = profileMapper.covertProfileToMyInfoResponseDTO(profile);
            return Response.success("Show information.")
                    .withData(responseDTO);
        }
    }
}
