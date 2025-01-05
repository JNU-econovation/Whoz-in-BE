package com.whoz_in.main_api.shared.event;

import org.springframework.context.ApplicationEventPublisher;

public class Events {

    private static ApplicationEventPublisher publisher;

    static void setEventPublisher(ApplicationEventPublisher context) {
        publisher = context;
    }

    public static void raise(Event event) {
        if(publisher!=null)
            publisher.publishEvent(event);
    }


}
