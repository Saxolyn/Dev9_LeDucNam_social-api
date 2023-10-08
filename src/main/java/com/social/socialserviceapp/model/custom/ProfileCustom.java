package com.social.socialserviceapp.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProfileCustom {

    private Long userId;
    private String username;
    private String realName;
    private String livePlace;

    public ProfileCustom(Long userId, String username, String realName, String livePlace){
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.livePlace = livePlace;
    }
}
