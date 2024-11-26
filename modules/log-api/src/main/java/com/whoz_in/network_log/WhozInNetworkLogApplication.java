package com.whoz_in.network_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.whoz_in"})
public class WhozInNetworkLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhozInNetworkLogApplication.class, args);
    }

}
