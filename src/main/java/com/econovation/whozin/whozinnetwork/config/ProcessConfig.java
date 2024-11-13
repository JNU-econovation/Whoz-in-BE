package com.econovation.whozin.whozinnetwork.config;

import java.util.Arrays;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProcessConfig {

    @Value("${network.process.command}")
    private String command;

    @Getter
    @Value("${network.process.password}")
    private String password;

    public ProcessConfig() {}

    public String[] getCommand(){
        Arrays.stream(command.split(" "))
                .forEach(System.out::println);
        return command.split(" ");
    }

}
