package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.MultipleFileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;


public class MultipleFileTypeValidator implements ConstraintValidator<MultipleFileType, MultipartFile[]> {

    @Override
    public void initialize(MultipleFileType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        List<Boolean> result = new ArrayList<>();
        if (multipartFile == null || multipartFile.length == 0) return true;
        for (MultipartFile file : multipartFile) {
            result.add(isSupportedContentType(file.getContentType()));
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return result.stream()
                    .allMatch(b -> b);
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg");
    }

}
