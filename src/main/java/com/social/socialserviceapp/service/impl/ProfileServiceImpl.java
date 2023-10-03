package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.mapper.ProfileMapper;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.social.socialserviceapp.util.FileUploadUtil.saveFile;
import static org.springframework.util.StringUtils.cleanPath;

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
                                      MultipartFile multipartFile) throws IOException {
        Profile profile = profileMapper.convertRequestDTOToProfile(requestDTO, multipartFile);
        profileRepository.save(profile);
        saveFile(profile.getAvatar(), multipartFile);
        return Response.success("Updated information successfully");
    }
}
