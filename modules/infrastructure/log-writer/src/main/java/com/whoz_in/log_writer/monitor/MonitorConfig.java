package com.whoz_in.log_writer.monitor;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MonitorConfig {
    private final String command;
    private final String sudoPassword;
    public MonitorConfig(@Value("${network.process.command.monitor}") String command,
            @Value("${network.process.password}") String sudoPassword) {
        this.command = command;
        this.sudoPassword = sudoPassword;
    }
}
