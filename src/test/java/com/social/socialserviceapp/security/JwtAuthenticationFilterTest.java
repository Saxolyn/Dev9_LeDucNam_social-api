package com.social.socialserviceapp.security;

import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenValidator jwtTokenValidator;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private JwtTokenProvider provider;

    @Mock
    UserDetailsService userDetailsService;

    @BeforeEach
    void setUp(){
        this.provider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
        jwtAuthenticationFilter.setTokenHeader("Authorization");
        jwtAuthenticationFilter.setTokenHeaderPrefix("Bearer");
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void doFilterInternal() throws ServletException, IOException{
        String token = "Bearer " + provider.createToken(new CustomUserDetails(new User()));
        Mockito.when(request.getHeader(ArgumentMatchers.anyString()))
                .thenReturn(token);
        token = token.replace("Bearer", "");
        Mockito.when(jwtTokenValidator.validateToken(ArgumentMatchers.anyString()))
                .thenReturn(true);
        Mockito.when(jwtTokenProvider.getUsernameFromJWT(ArgumentMatchers.anyString()))
                .thenReturn("kyrios");
        Mockito.when(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(new CustomUserDetails(new User()));
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
    }

    @Test
    void doFilterInternal_ifJwtNull() throws ServletException, IOException{
        try {
            String token = "Bearer " + provider.createToken(new CustomUserDetails(new User()));
            Mockito.when(request.getHeader(ArgumentMatchers.anyString()))
                    .thenReturn(token);
            Mockito.doThrow(NullPointerException.class)
                    .when(jwtTokenValidator)
                    .validateToken(ArgumentMatchers.anyString());
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        } catch (NullPointerException e) {
            Assertions.assertThrows(NullPointerException.class,
                    () -> jwtTokenValidator.validateToken(ArgumentMatchers.anyString()));
        }
    }
}