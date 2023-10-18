package com.social.socialserviceapp.model.dto.request;

import com.social.socialserviceapp.validation.annotation.NullOrBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(example = "{\n" +
        "  \"realName\": \"Le Duc Nam\",\n" +
        "  \"birthDate\": \"2002-07-4\",\n" +
        "  \"occupation\": \"Ban ma tuy\",\n" +
        "  \"livePlace\": \"Trung Quoc\"\n" +
        "}",type = "object")
public class UpdateInformationRequestDTO {

    @NullOrBlank(message = "must not be null or blank")
    private String realName;
    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "[birthDate] invalid date format.")
    private String birthDate;
    @NullOrBlank(message = "must not be null or blank")
    private String occupation;
    @NullOrBlank(message = "must not be null or blank")
    private String livePlace;

}
