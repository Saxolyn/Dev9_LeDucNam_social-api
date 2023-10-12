package com.social.socialserviceapp.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {

    @NotNull(message = "[username] must not be null.")
    @NotEmpty(message = "[username] must not be empty.")
    private String username;

    @NotNull(message = "[password] must not be null.")
    @NotEmpty(message = "[password] must not be empty.")
    private String password;
}
