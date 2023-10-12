package com.social.socialserviceapp.result;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.socialserviceapp.util.Constants;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String timestamp;
    private String type;
    private int status;
    private String message;
    private String errors;
    private Object data;
    private String cause;
    private String path;

    public Response() {
    }

    public Response(String type, int status, String message, Object data, String cause, String path) {
        this.timestamp = Instant.now()
                .toString();
        this.type = type;
        this.status = status;
        this.message = message;
        this.data = data;
        this.cause = cause;
        this.path = path;
    }

    public Response(String type, int status, String message, String errors, String cause, String path) {
        this.timestamp = Instant.now()
                .toString();
        this.type = type;
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.cause = cause;
        this.path = path;
    }

//    public Response(Object data, String cause, String path) {
//        this.timestamp = Instant.now()
//                .toString();
//        this.status = HttpStatus.BAD_REQUEST.value();
//        this.type = Constants.RESPONSE_TYPE.WARNING;
//        this.data = data;
//        this.cause = cause;
//        this.path = path;
//    }

    public Response(String type, int status) {
        this.type = type;
        this.status = status;
    }

    public static Response success(String message) {
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, HttpStatus.OK.value(), message, null, null, null);
    }

    public static Response success() {
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, HttpStatus.OK.value());
    }

    public Response withData(Object data) {
        this.data = data;
        return this;
    }

    public Response withMessage(String message) {
        this.message = message;
        return this;
    }
}
