package com.fs.filemarket.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.fs.filemarket.api.domain.file")
@SpringBootApplication(scanBasePackages = "com.fs.filemarket.api.domain.file")
public class SpringBootAwsS3Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAwsS3Application.class, args);
    }
}
