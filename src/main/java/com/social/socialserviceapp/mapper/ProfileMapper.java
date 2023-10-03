package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.request.UserRequestDTO;
import com.social.socialserviceapp.model.dto.response.UserResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.FileUploadUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.social.socialserviceapp.util.FileUploadUtil.saveFile;
import static org.springframework.util.StringUtils.cleanPath;

@Component
public class ProfileMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    public Profile convertRequestDTOToProfile(UpdateInformationRequestDTO requestDTO, MultipartFile multipartFile) {

        try {
            String fileName = cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            return modelMapper.createTypeMap(requestDTO, Profile.class).setPostConverter(converter -> {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                Optional.ofNullable(userService.findUserByUsername(username)
                                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND)))
                        .ifPresent(u -> converter.getDestination().setUserId(u.getId()));
                String result = RandomStringUtils.randomAlphanumeric(8) + "-" + fileName;
                converter.getDestination().setAvatar(result);
                return converter.getDestination();
            }).map(requestDTO);
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

}
