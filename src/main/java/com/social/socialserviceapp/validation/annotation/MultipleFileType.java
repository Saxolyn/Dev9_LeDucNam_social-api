package com.social.socialserviceapp.validation.annotation;

import com.social.socialserviceapp.validation.validator.MultipleFileTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipleFileTypeValidator.class)
@Documented
public @interface MultipleFileType {

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    String message() default "Only PNG or JPG images are allowed.";
}
