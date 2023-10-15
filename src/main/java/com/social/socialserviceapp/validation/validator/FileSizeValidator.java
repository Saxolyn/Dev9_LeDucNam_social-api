package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private static final long FILE_SIZE = 1048576L;

    private long maxSizeInMB;

    @Override
    public void initialize(FileSize fileSize){
        this.maxSizeInMB = fileSize.maxSizeInMB();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext){
        if (multipartFile == null) return true;
        return multipartFile.getSize() < FILE_SIZE;
    }
}
