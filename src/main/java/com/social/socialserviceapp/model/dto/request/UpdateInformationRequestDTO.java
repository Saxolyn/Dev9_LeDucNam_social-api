package com.social.socialserviceapp.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInformationRequestDTO {

    private String realName;
    private String birthDate;
    private String occupation;
    private String livePlace;

}
