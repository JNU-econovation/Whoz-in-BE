package com.whoz_in.network_log.domain.managed;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManagedConfig {

    @Value("${network.process.command.managed}")
    private String mDnsCommand;

    @Getter
    @Value("${network.process.password}")
    private String password;

    public ManagedConfig() {}

    public String[] mDnsCommand(){
        return mDnsCommand.split(" ");
    }

}
