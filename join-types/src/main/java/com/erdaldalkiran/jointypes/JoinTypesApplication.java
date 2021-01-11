package com.erdaldalkiran.jointypes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.erdaldalkiran"})
public class JoinTypesApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoinTypesApplication.class, args);
    }

}
