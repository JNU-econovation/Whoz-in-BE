package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.config.NetworkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelHopperConfig {
    private final NetworkConfig config;
    private final String sudoPassword;

    public ChannelHopperConfig(@Autowired NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.config = config;
        this.sudoPassword = sudoPassword;
    }

    @Bean
    public ChannelHopper channelHopper(){
        return config.getNetworkInterfaces().stream()
                .filter(ni -> ni.getMode().equals("monitor"))
                .findAny()
                .map(networkInterface -> new ChannelHopper(networkInterface, sudoPassword))
                .orElse(null);
    }
}
