package com.social.socialserviceapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private String content;
    private List<String> images;
//    private PostStatus status;
    private String createdBy;
    private String createdDate;

}
