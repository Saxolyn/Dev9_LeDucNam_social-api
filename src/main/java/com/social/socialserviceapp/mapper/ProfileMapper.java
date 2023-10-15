package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.entities.Profile;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.service.UserService;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
        } catch (Exception me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

    public MyInfoResponseDTO covertProfileToMyInfoResponseDTO(Profile profile){
        try {
            return MyInfoResponseDTO.builder()
                    .avatar(profile.getAvatar())
                    .birthDate(CommonUtil.convertDateToString(profile.getBirthDate()))
                    .livePlace(profile.getLivePlace())
                    .occupation(profile.getOccupation())
                    .realName(profile.getRealName())
                    .build();
        } catch (Exception me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

}
