package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
