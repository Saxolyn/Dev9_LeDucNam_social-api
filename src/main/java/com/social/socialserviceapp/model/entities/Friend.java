package com.social.socialserviceapp.model.entities;

import com.social.socialserviceapp.model.common.BaseEntity;
import com.social.socialserviceapp.model.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friends")
public class Friend extends BaseEntity {

    @Column(name = "base_user_id")
    private Long baseUserId;
    @Column(name = "other_user_id")
    private Long otherUserId;
    @Column(name = "sent_on")
    private LocalDateTime sentOn;
    @Column
    private FriendStatus status;

}
