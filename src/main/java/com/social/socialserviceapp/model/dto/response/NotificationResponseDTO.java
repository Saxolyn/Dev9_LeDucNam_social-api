package com.social.socialserviceapp.model.dto.response;

import com.social.socialserviceapp.model.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String descriptions;
    private NotificationStatus status;

}
