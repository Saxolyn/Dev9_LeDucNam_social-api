package com.social.socialserviceapp.model.entities;

import com.social.socialserviceapp.model.common.BaseEntity;
import com.social.socialserviceapp.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;
}
