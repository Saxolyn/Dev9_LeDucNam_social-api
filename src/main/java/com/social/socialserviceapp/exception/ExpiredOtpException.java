package com.social.socialserviceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class ExpiredOtpException extends SystemException {

    public ExpiredOtpException(String message){
        super(message);
    }
}
