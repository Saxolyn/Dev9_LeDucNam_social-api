package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.MatchPassword;
import com.social.socialserviceapp.validation.annotation.StrongPassword;
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
public class ResetPasswordRequestDTO {

    @StrongPassword
    private String password;

    private String rePassword;
}
