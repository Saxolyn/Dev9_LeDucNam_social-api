package com.social.socialserviceapp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUploadUtil {
    public static String handleImageUpload(MultipartFile multipartFile) throws IOException{
        String name = multipartFile.getOriginalFilename();
        log.info("File name: {}", name);
        String type = multipartFile.getContentType();
        log.info("File type: {}", type);
        long size = multipartFile.getSize();
        log.info("File size: {}", size);
        Path folder = Paths.get("src/main/resources/file_upload");
        String filename = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        Path file = Files.createTempFile(folder, filename + "-", "." + extension);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
            return file.getFileName()
                    .toString();
        } catch (IOException ioe) {
            throw new IOException("Error saving uploaded file: " + filename, ioe);
        }
    }
}
