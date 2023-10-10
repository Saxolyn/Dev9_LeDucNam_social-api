package com.social.socialserviceapp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendRequestsResponseDTO {

    private Long userId;
    private String username;
    private String realName;
    @JsonProperty(value = "wasSentOn")
    private LocalDateTime sendOn;

}
