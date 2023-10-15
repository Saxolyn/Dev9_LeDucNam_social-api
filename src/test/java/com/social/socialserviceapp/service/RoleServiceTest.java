package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.enums.RoleName;
import com.social.socialserviceapp.repository.RoleRepository;
import com.social.socialserviceapp.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;


    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void findByRoleName(){
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.of(new Role()));
        roleService.findByRoleName(RoleName.ROLE_USER);
    }
}