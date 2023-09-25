package com.social.socialserviceapp.exception;

import lombok.Data;

@Data
public class SocialAppException extends SystemException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public SocialAppException(String code, String message){
        this.code = code;
        this.message = message;
    }
}
