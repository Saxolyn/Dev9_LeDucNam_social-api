package com.social.socialserviceapp.result;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.socialserviceapp.util.Constants;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String timestamp;
    private String type;
    private int status;
    private String message;
    private Object data;
    private String cause;
    private String path;

    public Response(){
    }

    public Response(String type, int status, String message, Object data, String cause, String path){
        this.timestamp = Instant.now()
                .toString();
        this.type = type;
        this.status = status;
        this.message = message;
        this.data = data;
        this.cause = cause;
        this.path = path;
    }

    public Response(Object data, String cause, String path){
        this.data = data;
        this.cause = cause;
        this.path = path;
    }

    public Response(String type, int status){
        this.type = type;
        this.status = status;
    }

    public static Response success(String message){
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, HttpStatus.OK.value(), message, null, null, null);
    }

    public static Response success(){
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, HttpStatus.OK.value());
    }

    //    public Response(String type, String code, String message, Object data){
//        this.type = type;
//        this.code = code;
//        this.message = message;
//        this.data = data;
//    }
//
//    public Response(String type, String code, Object data){
//        this.type = type;
//        this.code = code;
//        this.data = data;
//    }
//
//    public Response(String type, String code){
//        this.type = type;
//        this.code = code;
//    }

//    public Response(String type){
//        this.type = type;
//    }
//
//    public static Response success(String code){
//        return new Response(Constants.RESPONSE_TYPE.SUCCESS, code);
//    }
//
//    public static Response success(){
//        return new Response(Constants.RESPONSE_TYPE.SUCCESS);
//    }
//
//    public static Response error(String code){
//        return new Response(Constants.RESPONSE_TYPE.ERROR, code);
//    }
//

    //
//    public static Response warning(String code){
//        return new Response(Constants.RESPONSE_TYPE.WARNING, code);
//    }
//
//    public static Response success(String code, String message){
//        return new Response(Constants.RESPONSE_TYPE.SUCCESS, code, message, null);
//    }
//
//    public static Response invalidPermission(){
//        return new Response(Constants.RESPONSE_TYPE.ERROR, "invalidPermission");
//    }
//
    public Response withData(Object data){
        this.data = data;
        return this;
    }

    public Response withMessage(String message){
        this.message = message;
        return this;
    }
}
