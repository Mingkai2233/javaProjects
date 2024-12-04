package com.sky.utils;

import com.sky.properties.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
@Component
public class MinioUtil {
    private final MinioClient minioClient;
    private final String bucketName;
    private final String endpoint;

    MinioUtil(MinioProperties properties, MinioClient client){
        this.minioClient = client;
        bucketName = properties.getBucketName();
        endpoint = properties.getEndpoint();
    }
    public String uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = uuid + suffix;
        String path =  endpoint+"/"+bucketName + "/" + fileName;
        PutObjectArgs args = PutObjectArgs.builder().object(fileName).bucket(bucketName).stream(file.getInputStream(), file.getSize(), -1).build();
        minioClient.putObject(args);
        return path;
    }

//    public InputStream getFile(String objectName) {
//        try {
//            // 文件是否存在
//            minioClient.statObject(bucket, objectName);
//            // 获取文件
//            return minioClient.getObject(bucket, objectName);
//        } catch (Exception e) {
//            logger.error("{}文件获取失败", objectName);
//            return null;
//        }
//    }
}
