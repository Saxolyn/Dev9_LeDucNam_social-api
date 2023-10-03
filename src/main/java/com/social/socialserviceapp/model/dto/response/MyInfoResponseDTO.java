package com.social.socialserviceapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoResponseDTO {

    private String realName;
    private String birthDate;
    private String occupation;
    private String livePlace;
    private String avatar;
}
