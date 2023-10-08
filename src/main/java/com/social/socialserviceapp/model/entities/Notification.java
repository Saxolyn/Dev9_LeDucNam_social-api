package com.social.socialserviceapp.model.entities;

import com.social.socialserviceapp.model.common.BaseEntity;
import com.social.socialserviceapp.model.enums.NotificationStatus;
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
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Column
    private String descriptions;
    @Column(name = "friend_id")
    private Long friendId;
    @Column(name = "post_id")
    private Long postId;
    @Column
    private NotificationStatus status;
}
