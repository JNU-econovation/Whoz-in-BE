package com.whoz_in.log_writer.managed.mdns;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MdnsConfig {
    private final List<String> mDnsCommands;
    private final String password;

    public MdnsConfig(
            @Value("${network.process.command.managed.mdns}") String rawMdnsCommands,
            @Value("${network.process.password}") String sudoPassword) {
        this.mDnsCommands = Arrays.asList(rawMdnsCommands.split(","));
        this.password = sudoPassword;
    }

}
