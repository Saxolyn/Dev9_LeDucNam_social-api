package com.social.socialserviceapp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowMyFriendsResponseDTO {

    private Long userId;

    private String username;

//    @JsonProperty(value = "userId")
//    private Long baseUserId;
}
