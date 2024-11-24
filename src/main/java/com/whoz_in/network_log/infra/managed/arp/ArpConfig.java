package com.whoz_in.network_log.infra.managed.arp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ArpConfig {

    private final String command;
    private final String password;

    public ArpConfig(
            @Value("${network.process.command.managed.arp}") final String command,
            @Value("${network.process.password}") final String password
    ){
        this.command = command;
        this.password = password;
    }



}
