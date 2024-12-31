package com.whoz_in.network_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = {"com.whoz_in"})
public class NetworkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkApiApplication.class, args);
    }

}
