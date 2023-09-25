package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
