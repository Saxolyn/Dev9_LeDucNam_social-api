package com.social.socialserviceapp.advice;

import com.social.socialserviceapp.exception.*;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.result.ValidationError;
import com.social.socialserviceapp.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SocialExceptionHandler {

    private String resolvePathFromWebRequest(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest()
                    .getRequestURI();
        } catch (Exception ex) {
            return null;
        }
    }

    @ExceptionHandler(value = InvalidTokenRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Response handleInvalidTokenRequestException(InvalidTokenRequestException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_NOT_ACCEPTABLE, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = InvalidOtpException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response handleInvalidOtpException(InvalidOtpException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = ExpiredOtpException.class)
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
    public Response handleExpiredOtpException(ExpiredOtpException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_GONE, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_NOT_FOUND, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = MappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleMappingException(MappingException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = SocialAppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleSocialAppException(SocialAppException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response handleNullPointerException(NullPointerException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                ex.getMessage(), null, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response processValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        List<ValidationError> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> new ValidationError(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new Response(Constants.RESPONSE_TYPE.WARNING, HttpServletResponse.SC_BAD_REQUEST, "Validation failed.",
                validationErrors, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

}
