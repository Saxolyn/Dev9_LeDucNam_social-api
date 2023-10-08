package com.social.socialserviceapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProfileResponseDTO {
    private Long userId;
    private String realName;
    private String livePlace;
}
