package com.social.socialserviceapp.model.dto.response;

import com.social.socialserviceapp.model.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowMyPostResponseDTO {

    private Long id;
    private String content;
    private List<String> images;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private PostStatus status;
}
