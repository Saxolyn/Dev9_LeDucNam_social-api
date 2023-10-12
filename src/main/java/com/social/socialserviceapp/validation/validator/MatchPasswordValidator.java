package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.model.dto.request.ResetPasswordRequestDTO;
import com.social.socialserviceapp.validation.annotation.MatchPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, ResetPasswordRequestDTO> {

    @Override
    public void initialize(MatchPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ResetPasswordRequestDTO value, ConstraintValidatorContext constraintValidatorContext) {
        String password = value.getPassword();
        String rePassword = value.getRePassword();
        return Objects.equals(password, rePassword);
    }

}
