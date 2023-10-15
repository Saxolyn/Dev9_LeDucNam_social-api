package com.social.socialserviceapp.validation.annotation;

import com.social.socialserviceapp.validation.validator.FileTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface FileType {

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    long maxSizeInMB() default 512;

    String message() default "Only PNG or JPG images are allowed.";

}
