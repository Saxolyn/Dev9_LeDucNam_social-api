package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.mapper.NotificationMapper;
import com.social.socialserviceapp.model.dto.response.NotificationResponseDTO;
import com.social.socialserviceapp.model.entities.Notification;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.NotificationRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.NotificationService;
import com.social.socialserviceapp.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Response showNotifications(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));
        List<Notification> notifications = notificationRepository.getNotificationsByUserId(user.getId());
        if (!CommonUtil.isNullOrEmpty(notifications)) {
            List<NotificationResponseDTO> notificationResponseDTOS = notificationMapper.convertNotificationLstToNotificationResponseDTOLst(
                    notifications);
            return Response.success("Show notifications.")
                    .withData(notificationResponseDTOS);
        } else {
            return Response.success("U don't have any notifications.");
        }
    }
}
