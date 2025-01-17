package com.whoz_in.main_api.shared.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventConfiguration {

    private final ApplicationEventPublisher publisher;
    @Bean
    public InitializingBean eventInitializer(){return () -> Events.setEventPublisher(publisher);};

}
