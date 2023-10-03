package com.social.socialserviceapp.model.entities;

import com.social.socialserviceapp.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity<Long> {

    @Column(name = "real_name")
    private String realName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "live_place")
    private String livePlace;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "user_id")
    private Long userId;
}
