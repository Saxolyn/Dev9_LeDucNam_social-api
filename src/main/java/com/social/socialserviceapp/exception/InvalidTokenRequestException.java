package com.social.socialserviceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {
    public InvalidTokenRequestException(String tokenType, String token, String message){
        super(String.format("%s: [%s] token: [%s] ", message, tokenType, token));
    }
}
