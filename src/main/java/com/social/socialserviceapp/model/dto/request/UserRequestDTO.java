package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.EmailExists;
import com.social.socialserviceapp.validation.annotation.NullOrBlank;
import com.social.socialserviceapp.validation.annotation.StrongPassword;
import com.social.socialserviceapp.validation.annotation.UsernameExists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @Schema(example = "kyrios", type = "string")
    @NullOrBlank(message = "[username] must not be null or blank")
    @UsernameExists
    private String username;

    @Schema(example = "ur_mail@gmail.com", type = "string")
    @NullOrBlank(message = "[email] must not be null or blank")
    @Email()
    @EmailExists
    private String email;

    @Schema(example = "aduchat123!@#", type = "string")
    @NullOrBlank(message = "[password] must not be null or blank")
    @StrongPassword
    private String password;

}
