package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.StrongPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public void initialize(StrongPassword constraintAnnotation){
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext){
        if (value != null && !value.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$")) {
            return false;
        }
        return true;
    }

}
