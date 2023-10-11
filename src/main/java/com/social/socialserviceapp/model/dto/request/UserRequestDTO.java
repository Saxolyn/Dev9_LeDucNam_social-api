package com.social.socialserviceapp.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {

    @NotNull
    @NotEmpty
    private String username;

    @Email
    private String email;

    @NotNull
    private String password;

}
