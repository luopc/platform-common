package com.luopc.platform.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Slf4j
@SpringBootApplication
@EnableScheduling
public class PlatformWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformWebApplication.class, args);
    }
}
