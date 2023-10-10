package com.social.socialserviceapp.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PostCustom {

    private Long id;
    private String content;
    private String images;
    private int likes;
    private int comments;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public PostCustom(Long id, String content, String images, int likes, int comments, String createdBy,
                      LocalDateTime createdDate, String lastModifiedBy, LocalDateTime lastModifiedDate){
        this.id = id;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }
}
