package com.whoz_in.master_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.whoz_in"})
public class MasterApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterApiApplication.class, args);
    }

}