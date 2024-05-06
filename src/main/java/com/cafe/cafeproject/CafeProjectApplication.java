package com.cafe.cafeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CafeProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeProjectApplication.class, args);
    }

}
