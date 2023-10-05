package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.request.UpdateInformationRequestDTO;
import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
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
//            if (modelMapper.getTypeMap(UpdateInformationRequestDTO.class, Profile.class) == null) {
//                modelMapper.createTypeMap(requestDTO, Profile.class)
//                        .setPostConverter(converter -> {
//                            String username = SecurityContextHolder.getContext()
//                                    .getAuthentication()
//                                    .getName();
////                            Optional.ofNullable(userService.findUserByUsername(username)
////                                            .orElseThrow(
////                                                    () -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND)))
////                                    .ifPresent(u -> converter.getDestination()
////                                            .setUserId(u.getId()));
//
//                            if (profile != null) {
//                                converter.getDestination()
//                                        .setId(profile.getId());
//                            }
//                            return converter.getDestination();
//                        });
//            }
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

}
