package com.social.socialserviceapp.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {

    private String field;
    private Object object;
    private String message;

    public ValidationError(String field, String message){
        this.field = field;
        this.message = message;
    }

    public ValidationError(Object object, String message){
        this.object = object;
        this.message = message;
    }
}
