package com.sky.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class LocalStroage {
    private static final String STROAGE_PATH="D:\\javaProjects\\sky-take-out-storage\\src\\main\\resources\\static";
    public String upload(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = uuid + suffix;
        String path =  STROAGE_PATH + "/" + fileName;
        file.transferTo(new File(path));
        return path;
    }
}
