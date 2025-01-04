package com.whoz_in.main_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.whoz_in"})
@ConfigurationPropertiesScan
public class MainApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApiApplication.class, args);
    }

}