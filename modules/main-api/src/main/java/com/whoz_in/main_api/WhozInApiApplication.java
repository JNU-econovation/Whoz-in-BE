package com.whoz_in.main_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.whoz_in"})
public class WhozInApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhozInApiApplication.class, args);
    }

}