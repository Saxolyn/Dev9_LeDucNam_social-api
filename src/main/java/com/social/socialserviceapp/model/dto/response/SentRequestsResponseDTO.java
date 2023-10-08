package com.social.socialserviceapp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SentRequestsResponseDTO {

    private Long userId;
    private String username;
    private String realName;
    @JsonProperty(value = "wasSentOn")
    private String sendOn;

}
