package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.FileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    @Override
    public void initialize(FileType constraintAnnotation){

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context){
        String contentType = value.getContentType();
        if (!isSupportedContentType(contentType)) {
            return false;
        }
        return true;
    }

    private boolean isSupportedContentType(String contentType){
        return contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg");
    }

}
