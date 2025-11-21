package com.rey.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.rey.job")
public class JobApplication {
    public static void main(String[] args) {

        SpringApplication.run(JobApplication.class);

    }
}
