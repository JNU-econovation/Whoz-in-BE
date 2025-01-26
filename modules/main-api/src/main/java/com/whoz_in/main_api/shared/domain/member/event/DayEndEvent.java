package com.whoz_in.main_api.shared.domain.member.event;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import com.whoz_in.main_api.shared.event.Events;
import org.springframework.scheduling.annotation.Scheduled;

public record DayEndEvent() implements ApplicationEvent {

    // 매일 자정에 이벤트 발생
    @Scheduled(cron = "0 0 0 * * *")
    public void raiseDayEndEvent(){
        Events.raise(new DayEndEvent());
    }

}
