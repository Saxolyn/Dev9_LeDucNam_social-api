package com.social.socialserviceapp.model.dto.request;

import lombok.Data;

@Data
public class VerifyRequestDTO {

    private String username;
    private String otp;
}
