package com.social.socialserviceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class NotFoundException extends SystemException {

    public NotFoundException(String message){
        super(message);
    }
}
