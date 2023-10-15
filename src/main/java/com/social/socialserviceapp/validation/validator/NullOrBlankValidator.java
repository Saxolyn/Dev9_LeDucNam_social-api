package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.NullOrBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrBlankValidator implements ConstraintValidator<NullOrBlank, String> {

    @Override
    public void initialize(NullOrBlank constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (null == value) {
            return false;
        }
        if (value.length() == 0) {
            return false;
        }
        return true;
    }
}
