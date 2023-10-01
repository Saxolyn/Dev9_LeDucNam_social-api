package com.social.socialserviceapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.socialserviceapp.result.Response;

public class CommonUtil {

    public static String convertObjectToJson(Object object) throws JsonProcessingException{
        if (object == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        }
    }

    public static Response getSuccessResult(Object object){
        return Response.success(null)
                .withData(object);
    }
}
