package com.whoz_in.network_log.domain.monitor;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitorConfig {
    @Value("${network.process.password}")
    private String sudoPassword;

    @Value("${network.interface.monitor}")
    private String monitorInterfaceName;

    @Bean
    public Process monitorTShark(){
        ProcessBuilder pb = new ProcessBuilder(
                "sudo", "-S",
                "tshark",
                "-i", monitorInterfaceName,
                "-T", "fields",
                "-e", "ip.src",
                "-e", "eth.src"
        );
        pb.redirectErrorStream(true);
        Process process;
        try {
            process = pb.start();
            OutputStream os = process.getOutputStream();
            os.write((sudoPassword+"\n").getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("monitorTshark 시작 실패");
        }
        return process;
    }
}
