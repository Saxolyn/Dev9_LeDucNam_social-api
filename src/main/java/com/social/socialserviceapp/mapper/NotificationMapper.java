package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.NotificationResponseDTO;
import com.social.socialserviceapp.model.entities.Notification;
import com.social.socialserviceapp.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProfileRepository profileRepository;

    public List<NotificationResponseDTO> convertNotificationLstToNotificationResponseDTOLst(
            List<Notification> notifications){
        return notifications.stream()
                .map(notification -> {
                    if (modelMapper.getTypeMap(Notification.class, NotificationResponseDTO.class) == null) {
                        modelMapper.createTypeMap(notification, NotificationResponseDTO.class)
                                .setPostConverter(MappingContext::getDestination);
                    }
                    return modelMapper.map(notification, NotificationResponseDTO.class);
                })
                .collect(Collectors.toList());
    }
}
