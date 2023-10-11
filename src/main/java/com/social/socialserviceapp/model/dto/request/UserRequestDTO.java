package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.EmailExists;
import com.social.socialserviceapp.validation.annotation.UsernameExists;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {

    @NotNull
    @NotEmpty
    @UsernameExists
    private String username;

    @NotNull
    @NotEmpty
    @Email
    @EmailExists
    private String email;

    @NotNull
    @NotEmpty
    private String password;

}
