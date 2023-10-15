package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.enums.RoleName;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    public Role findByRoleName(RoleName roleName);
}
