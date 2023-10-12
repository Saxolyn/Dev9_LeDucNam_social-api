package com.social.socialserviceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidOtpException extends SystemException {

    public InvalidOtpException(String message){
        super(message);
    }
}
