package com.social.socialserviceapp.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CommentRequestDTO {

    @Schema(type = "string", example = "aduchat")
    private String content;
}
