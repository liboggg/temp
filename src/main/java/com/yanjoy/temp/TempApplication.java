package com.yanjoy.temp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TempApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempApplication.class, args);
    }

}
