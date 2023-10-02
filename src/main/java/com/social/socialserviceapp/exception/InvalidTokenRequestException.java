package com.social.socialserviceapp.exception;

public class InvalidTokenRequestException extends RuntimeException {
    public InvalidTokenRequestException(String tokenType, String token, String message){
        super(String.format("%s: [%s] token: [%s] ", message, tokenType, token));
    }
}
