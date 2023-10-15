package com.social.socialserviceapp.security;

import com.social.socialserviceapp.exception.InvalidTokenRequestException;
import com.social.socialserviceapp.model.CustomUserDetails;
import com.social.socialserviceapp.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;

    private JwtTokenValidator jwtTokenValidator;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        this.jwtTokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
        this.jwtTokenValidator = new JwtTokenValidator(jwtSecret);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void validateToken_throwExpiredJwtException() throws InterruptedException{
        String token = jwtTokenProvider.createToken(new CustomUserDetails(new User()));
        TimeUnit.MILLISECONDS.sleep(jwtExpiryInMs);
        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> jwtTokenValidator.validateToken(token));
        assertTrue(ex.getMessage()
                .contains("Token expired."));
    }

    @Test
    void validateToken_throwMalformedJwtException(){
        try {
            boolean result = jwtTokenValidator.validateToken("aduchat");
            assertTrue(result);
        } catch (InvalidTokenRequestException e) {
            assertEquals("Malformed jwt token: [JWT] token: [aduchat] ", e.getMessage());
        }
    }

    @Test
    void validateToken_throwSignatureException(){
        String token = jwtTokenProvider.createToken(new CustomUserDetails(new User()));
        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> jwtTokenValidator.validateToken(token + "-adu"));
        assertTrue(ex.getMessage()
                .contains("Incorrect signature"));
    }

    @Test
    void validateToken_throwIllegalArgumentException(){
        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> jwtTokenValidator.validateToken(""));
        assertTrue(ex.getMessage()
                .contains("Illegal argument token"));
    }
}