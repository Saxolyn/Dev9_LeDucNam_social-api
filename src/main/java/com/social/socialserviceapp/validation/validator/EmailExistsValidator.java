package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.validation.annotation.EmailExists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailExists constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        boolean isEmailExists = userRepository.existsUserByEmail(value);
        if (isEmailExists) {
            return false;
        }
        return true;
    }
}
