package com.social.socialserviceapp.security;

import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.entities.Role;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.RoleName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 25000;

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp(){
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void createToken(){
    }

    @Test
    void getUsernameFromJWT(){
        String token = tokenProvider.createToken(stubCustomUser());
        assertEquals("kyrios", tokenProvider.getUsernameFromJWT(token));
    }

    @Test
    void getAuthoritiesFromJWT(){
        String token = tokenProvider.createToken(stubCustomUser());
        assertNotNull(tokenProvider.getAuthoritiesFromJWT(token));
    }

    private CustomUserDetails stubCustomUser(){
        User user = new User();
        user.setUsername("kyrios");
        user.setRoles(Collections.singleton(new Role(RoleName.ROLE_USER)));
        return new CustomUserDetails(user);
    }

}