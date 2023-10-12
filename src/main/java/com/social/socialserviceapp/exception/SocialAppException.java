package com.social.socialserviceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SocialAppException extends SystemException {

    public SocialAppException(String message){
        super(message);
    }
}
