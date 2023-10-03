package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.mapper.ProfileMapper;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.social.socialserviceapp.util.FileUploadUtil.handleImageUpload;

@Transactional
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public Response updateInformation(UpdateInformationRequestDTO requestDTO,
                                      MultipartFile multipartFile) throws IOException{
        Profile profile = profileMapper.convertRequestDTOToProfile(requestDTO, multipartFile);
        profile.setAvatar(handleImageUpload(multipartFile));
        profileRepository.save(profile);
        return Response.success("Updated information successfully");
    }

    @Override
    public byte[] showAvatar() throws IOException{
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByUsername(username);
        if (profile == null) {
            throw new NotFoundException("Profile not found.");
        } else {
            InputStream inputStream = getClass().getResourceAsStream("/file_upload/" + profile.getAvatar());
            return IOUtils.toByteArray(inputStream);
        }
    }

    @Override
    public Response showMyInfo(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Profile profile = profileRepository.findProfileByUsername(username);
        if (profile == null) {
            throw new NotFoundException("Profile not found.");
        } else {
            MyInfoResponseDTO responseDTO = profileMapper.covertProfileToMyInfoResponseDTO(profile);
            return Response.success("Show information.")
                    .withData(responseDTO);
        }
    }
}
