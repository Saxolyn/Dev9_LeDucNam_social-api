package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRoleName(RoleName roleName){
        return roleRepository.findByRoleName(roleName)
                .orElse(null);
    }
}
