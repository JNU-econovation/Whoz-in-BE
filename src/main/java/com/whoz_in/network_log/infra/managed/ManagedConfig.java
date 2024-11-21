package com.whoz_in.network_log.infra.managed;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManagedConfig {

    @Value("${network.process.command.managed}")
    private String mDnsCommands;

    @Getter
    @Value("${network.process.password}")
    private String password;

    public ManagedConfig() {}

    public String[] mDnsCommands(){
        return mDnsCommands.split(",");
    }

}
