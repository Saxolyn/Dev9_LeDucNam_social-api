package com.social.socialserviceapp.validation.validator;

import com.social.socialserviceapp.validation.annotation.MultipleFileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class MultipleFileSizeValidator implements ConstraintValidator<MultipleFileSize, MultipartFile[]> {

    private static final long FILE_SIZE = 1048576L;

    @Override
    public void initialize(MultipleFileSize fileSize){
    }

    @Override
    public boolean isValid(MultipartFile[] multipartFile, ConstraintValidatorContext constraintValidatorContext){
        List<Boolean> result = new ArrayList<>();
        if (multipartFile == null || multipartFile.length == 0) return true;
        for (MultipartFile file : multipartFile) {
            result.add(file.getSize() < FILE_SIZE);
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return result.stream()
                    .allMatch(b -> b);
        }
    }

}
