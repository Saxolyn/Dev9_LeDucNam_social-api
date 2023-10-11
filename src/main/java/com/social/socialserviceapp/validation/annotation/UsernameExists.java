package com.social.socialserviceapp.validation.annotation;

import com.social.socialserviceapp.validation.validator.UsernameExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameExistsValidator.class)
@Documented
public @interface UsernameExists {

    String message() default "Username already exists.";

    Class<?>[] groups() default {};

    boolean allowNull() default false;

    Class<? extends Payload>[] payload() default {};

}
