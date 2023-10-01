package com.social.socialserviceapp.advice;

import com.social.socialserviceapp.exception.ExpiredOtpException;
import com.social.socialserviceapp.exception.InvalidOtpException;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
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
    private static final Logger logger = LoggerFactory.getLogger(SocialExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response processValidationError(MethodArgumentNotValidException ex, WebRequest request){
        BindingResult result = ex.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();
        String data = processAllErrors(allErrors).stream()
                .collect(Collectors.joining("\n"));
        return new Response(data, ex.getClass()
                .getName(), resolvePathFromWebRequest(request));
    }

    private List<String> processAllErrors(List<ObjectError> allErrors){
        return allErrors.stream()
                .map(this::resolveLocalizedErrorMessage)
                .collect(Collectors.toList());
    }

    private String resolveLocalizedErrorMessage(ObjectError objectError){
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
        logger.info(localizedErrorMessage);
        return localizedErrorMessage;
    }

    private String resolvePathFromWebRequest(WebRequest request){
        try {
            return ((ServletWebRequest) request).getRequest()
                    .getRequestURI();
        } catch (Exception ex) {
            return null;
        }
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleBadCredentialsException(BadCredentialsException ex, WebRequest request){
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = InvalidOtpException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response handleInvalidOtpException(InvalidOtpException ex, WebRequest request){
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = ExpiredOtpException.class)
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
    public Response handleExpiredOtpException(ExpiredOtpException ex, WebRequest request){
        return new Response(Constants.RESPONSE_TYPE.ERROR, HttpServletResponse.SC_GONE, ex.getMessage(), null,
                ex.getClass()
                        .getName(), resolvePathFromWebRequest(request));
    }
}
