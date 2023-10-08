package com.social.socialserviceapp.model.entities;

import com.social.socialserviceapp.model.common.BaseEntity;
import com.social.socialserviceapp.model.enums.ReactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reacts")
public class React extends BaseEntity {

    @Column(name = "post_id")
    private Long postId;
    @Column(name = "comment_id")
    private Long commentId;
    @Column
    private ReactStatus status;

}
