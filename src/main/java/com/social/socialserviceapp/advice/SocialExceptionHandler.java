package com.social.socialserviceapp.advice;

import com.social.socialserviceapp.exception.*;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.result.ValidationError;
import com.social.socialserviceapp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SocialExceptionHandler {

    @Autowired
    private MessageSource messageSource;

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
    public Response handleInvalidTokenRequestException(InvalidTokenRequestException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_NOT_ACCEPTABLE, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = InvalidOtpException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleInvalidOtpException(InvalidOtpException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = ExpiredOtpException.class)
    @ResponseStatus(HttpStatus.GONE)
    public Response handleExpiredOtpException(ExpiredOtpException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_GONE, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_NOT_FOUND, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = MappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleMappingException(MappingException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = SocialAppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleSocialAppException(SocialAppException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleNullPointerException(NullPointerException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                ex.getMessage(), null, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_NOT_FOUND, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public Response processValidationError(MethodArgumentNotValidException ex, WebRequest request) {
//        List<ValidationError> errors = ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(error -> new ValidationError(((FieldError) error).getField(), error.getDefaultMessage()))
//                .collect(Collectors.toList());
//        return new Response(Constants.RESPONSE_TYPE.WARNING, HttpServletResponse.SC_BAD_REQUEST,
//                "Validation error. Check 'errors' field for details.", errors, ex.getClass()
//                .getName(), resolvePathFromWebRequest(request));
//    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleGeneralExceptions(Exception ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                ex.getMessage(), null, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleRuntimeExceptions(RuntimeException ex, WebRequest request) {
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                ex.getMessage(), null, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response processValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();
        String data = processAllErrors(allErrors).stream()
                .collect(Collectors.joining("\n"));
        return new Response(Constants.RESPONSE_TYPE.WARNING, HttpServletResponse.SC_BAD_REQUEST,
                "Validation error. Check 'errors' field for details.", data, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    private List<String> processAllErrors(List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(this::resolveLocalizedErrorMessage)
                .collect(Collectors.toList());
    }

    private String resolveLocalizedErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
//        logger.info(localizedErrorMessage);
        return localizedErrorMessage;
    }

}
