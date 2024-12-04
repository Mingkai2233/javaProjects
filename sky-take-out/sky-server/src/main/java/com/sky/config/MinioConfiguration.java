package com.sky.config;

import com.sky.properties.MinioProperties;
import io.minio.BucketArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Slf4j
@Configuration
public class MinioConfiguration {
    @Autowired
    private MinioProperties properties;

    @Bean
    public MinioClient minioClient() throws Exception {
        log.info("---------- Minio文件系统初始化加载 ----------");
        MinioClient minioClient = MinioClient.builder().endpoint(properties.getEndpoint()).
                credentials(properties.getAccessKey(), properties.getSecretKey()).build();
        // 判断Bucket是否存在

        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
        if (isExist) {
            log.info("---------- Minio文件系统Bucket已存在 ----------");
        } else {
            // 不存在创建一个新的Bucket
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build()  );
            log.info("---------- Minio文件系统Bucket已创建 ----------");
        }
        log.info("---------- Minio文件系统初始化完成 ----------");
        return minioClient;
    }
}
