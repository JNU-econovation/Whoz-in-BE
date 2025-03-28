package com.whoz_in.main_api.shared.domain.member.event;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import com.whoz_in.main_api.shared.event.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DayEndEvent implements ApplicationEvent {

    // 매일 자정에 이벤트 발생
    @Scheduled(cron = "0 0 6 * * *")
    public void raiseDayEndEvent(){
        Events.raise(this);
    }

}
