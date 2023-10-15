package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.NullOrBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NullOrBlank(message = "[username] must not be null or blank")
    private String username;

    @NullOrBlank(message = "[password] must not be null or blank")
    private String password;
}
