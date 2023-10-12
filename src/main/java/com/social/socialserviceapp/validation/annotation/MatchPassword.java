package com.social.socialserviceapp.validation.annotation;

import com.social.socialserviceapp.validation.validator.MatchPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchPasswordValidator.class)
@Documented
public @interface MatchPassword {

    String message() default "[password] and [rePassword] not match.";

    Class<?>[] groups() default {};

    boolean allowNull() default false;

    Class<? extends Payload>[] payload() default {};

}
