package com.social.socialserviceapp.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {

    @NotNull(message = "username must not be null")
    @NotEmpty(message = "username must not be empty")
    private String username;
    @NotNull(message = "email must not be null")
    private String email;
    @NotNull(message = "password must not be null")
    private String password;

}
