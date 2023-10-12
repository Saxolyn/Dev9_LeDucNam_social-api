package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.EmailExists;
import com.social.socialserviceapp.validation.annotation.StrongPassword;
import com.social.socialserviceapp.validation.annotation.UsernameExists;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {

    @NotNull(message = "[username] must not be null.")
    @NotEmpty(message = "[username] must not be empty.")
    @UsernameExists
    private String username;

    @NotNull(message = "[email] must not be null.")
    @NotEmpty(message = "[email] must not be empty.")
    @Email
    @EmailExists
    private String email;

    @NotNull(message = "[password] must not be null.")
    @NotEmpty(message = "[password] must not be empty.")
    @StrongPassword
    private String password;

}
