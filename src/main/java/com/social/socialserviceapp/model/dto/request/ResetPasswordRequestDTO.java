package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.MatchPassword;
import com.social.socialserviceapp.validation.annotation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MatchPassword
@Schema(example = "{\n" +
        "  \"password\": \"aduchat123!@#\",\n" +
        "  \"rePassword\": \"aduchat123!@#\"\n" +
        "}",type = "object")
public class ResetPasswordRequestDTO {

    @StrongPassword
    private String password;

    private String rePassword;
}
