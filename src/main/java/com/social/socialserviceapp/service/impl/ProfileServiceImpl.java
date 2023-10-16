package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.mapper.ProfileMapper;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import com.social.socialserviceapp.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.social.socialserviceapp.util.FileUploadUtil.handleImageUpload;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;

    @Override
    public Response updateInformation(UpdateInformationRequestDTO requestDTO) {
        Profile profile = profileMapper.convertRequestDTOToProfile(requestDTO);
        profileRepository.save(profile);
        return Response.success("Updated information successfully");
    }

    @Override
    public ResponseEntity<?> showAvatar() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByLastModifiedBy(username);
        if (profile == null) {
            throw new NotFoundException(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND);
        } else {
            try {
                Path path = Paths.get("src/main/resources/file_upload/", profile.getAvatar());
                String mimeType = Files.probeContentType(path);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mimeType))
                        .body(Files.readAllBytes(path));
            } catch (Exception ex) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Response.success("Where did ur avatar go?"));
            }
        }
    }

    @Override
    public Response showMyInfo() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByLastModifiedBy(username);
        if (profile == null) {
            throw new NotFoundException(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND);
        } else {
            MyInfoResponseDTO responseDTO = profileMapper.covertProfileToMyInfoResponseDTO(profile);
            return Response.success("Show information.")
                    .withData(responseDTO);
        }
    }

    @Override
    public Response showOtherInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Profile profile = profileRepository.findProfileByLastModifiedBy(user.getUsername());
        if (profile == null) {
            throw new NotFoundException(Constants.RESPONSE_MESSAGE.PROFILE_NOT_FOUND);
        } else {
            MyInfoResponseDTO responseDTO = profileMapper.covertProfileToMyInfoResponseDTO(profile);
            return Response.success("Show information.")
                    .withData(responseDTO);
        }
    }

    @Override
    public Response uploadAvatar(MultipartFile multipartFile) throws IOException {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByLastModifiedBy(username);
        if (profile == null) {
            profile = new Profile();
        }
        if (multipartFile != null) {
            profile.setAvatar(handleImageUpload(multipartFile));
        }
        profileRepository.save(profile);
        return Response.success("Uploaded avatar successfully.");
    }
}
