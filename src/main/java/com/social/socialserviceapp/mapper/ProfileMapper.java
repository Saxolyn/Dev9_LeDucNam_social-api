package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.dto.response.SearchProfileResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.Constants;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    public Profile convertRequestDTOToProfile(UpdateInformationRequestDTO requestDTO){
        try {
            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
            Profile profile = profileRepository.findProfileByLastModifiedBy(username);
            if (profile == null) {
                profile = new Profile();
            }
            profile.setRealName(requestDTO.getRealName());
            profile.setBirthDate(CommonUtil.convertStringToDate(requestDTO.getBirthDate()));
            profile.setOccupation(requestDTO.getOccupation());
            profile.setLivePlace(requestDTO.getLivePlace());
            return profile;
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MyInfoResponseDTO covertProfileToMyInfoResponseDTO(Profile profile){
        try {
            if (modelMapper.getTypeMap(Profile.class, MyInfoResponseDTO.class) == null) {
                modelMapper.createTypeMap(profile, MyInfoResponseDTO.class)
                        .setPostConverter(converter -> {
                            String dateStr = CommonUtil.convertDateToString(converter.getSource()
                                    .getBirthDate());
                            converter.getDestination()
                                    .setBirthDate(dateStr);
                            return converter.getDestination();
                        });
            }
            return modelMapper.map(profile, MyInfoResponseDTO.class);
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

    public List<SearchProfileResponseDTO> convertListProfilesToListSearchProfileResponseDTO(List<Profile> profiles){
        try {
            return profiles.stream()
                    .map(profile -> {
                        if (modelMapper.getTypeMap(Profile.class, SearchProfileResponseDTO.class) == null) {
                            modelMapper.createTypeMap(profile, SearchProfileResponseDTO.class)
                                    .setPostConverter(converter -> {
                                        converter.getDestination()
                                                .setUserId(userService.findUserByUsername(converter.getSource()
                                                                .getLastModifiedBy())
                                                        .get()
                                                        .getId());
                                        return converter.getDestination();
                                    });
                        }
                        return modelMapper.map(profile, SearchProfileResponseDTO.class);
                    })
                    .collect(Collectors.toList());
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }
}
