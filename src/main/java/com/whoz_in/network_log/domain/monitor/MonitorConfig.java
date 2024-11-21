package com.whoz_in.network_log.domain.monitor;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitorConfig {

    @Value("${network.process.command.monitor}")
    private String command;

    @Bean
    public Process monitorTShark(){
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("monitorTshark 시작 실패");
        }
        return process;
    }
}
