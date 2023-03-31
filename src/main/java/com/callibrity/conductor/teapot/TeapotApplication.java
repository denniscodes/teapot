package com.callibrity.conductor.teapot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication @Slf4j
public class TeapotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeapotApplication.class, args);
    }

}
