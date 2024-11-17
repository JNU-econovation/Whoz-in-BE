package com.whoz_in.network_log.domain.managed;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProcessConfig {

    @Value("${network.process.command}")
    private String mDnsCommand;

    @Getter
    @Value("${network.process.password}")
    private String password;

    public ProcessConfig() {}

    public String[] mDnsCommand(){
        return mDnsCommand.split(" ");
    }

}
