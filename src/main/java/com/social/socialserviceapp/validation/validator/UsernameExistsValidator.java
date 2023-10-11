package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.validation.annotation.UsernameExists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameExistsValidator implements ConstraintValidator<UsernameExists, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UsernameExists constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        boolean isUsernameExists = userRepository.existsUserByUsername(value);
        if (isUsernameExists) {
            return false;
        }
        return true;
    }

}
