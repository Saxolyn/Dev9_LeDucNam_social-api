package com.social.socialserviceapp.advice;

import com.social.socialserviceapp.exception.*;
import com.social.socialserviceapp.result.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SocialExceptionHandlerTest {

    @InjectMocks
    private SocialExceptionHandler socialExceptionHandler;

    @Mock
    private WebRequest request;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void handleInvalidTokenRequestException(){
        Response response = socialExceptionHandler.handleInvalidTokenRequestException(
                new InvalidTokenRequestException("JWT", "123", "Illegal argument token"), request);
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
    }

    @Test
    void handleBadCredentialsException(){
        Response response = socialExceptionHandler.handleBadCredentialsException(
                new BadCredentialsException("Invalid username or password."), request);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void handleInvalidOtpException(){
        Response response = socialExceptionHandler.handleInvalidOtpException(new InvalidOtpException("Invalid otp."),
                request);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    void handleExpiredOtpException(){
        Response response = socialExceptionHandler.handleExpiredOtpException(new ExpiredOtpException("Expired otp."),
                request);
        assertEquals(HttpStatus.GONE.value(), response.getStatus());
    }

    @Test
    void handleNotFoundException(){
        Response response = socialExceptionHandler.handleNotFoundException(new NotFoundException("Something not found"),
                request);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void handleMappingException(){
        Response response = socialExceptionHandler.handleMappingException(new MappingException("Mapping Error"),
                request);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void handleSocialAppException(){
        Response response = socialExceptionHandler.handleSocialAppException(new SocialAppException("Something wrong."),
                request);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void handleIllegalArgumentException(){
        Response response = socialExceptionHandler.handleIllegalArgumentException(
                new IllegalArgumentException("Illegal argument."), request);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void handleNullPointerException(){
        Response response = socialExceptionHandler.handleNullPointerException(new NullPointerException("Null value"),
                request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void handleUserNotFoundException(){
        Response response = socialExceptionHandler.handleUserNotFoundException(
                new UsernameNotFoundException("User not found."), request);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void handleGeneralExceptions(){
        Response response = socialExceptionHandler.handleGeneralExceptions(
                new Exception("Plz contact administrator."), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void handleRuntimeExceptions(){
        Response response = socialExceptionHandler.handleRuntimeExceptions(
                new RuntimeException("Something wrong during runtime."), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void handleConstraintViolationException(){
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        Response response = socialExceptionHandler.handleConstraintViolationException(
                new ConstraintViolationException(constraintViolations), request);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}