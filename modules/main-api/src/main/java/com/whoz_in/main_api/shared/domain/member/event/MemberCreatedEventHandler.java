package com.whoz_in.main_api.shared.domain.member.event;

import com.whoz_in.domain.member.event.MemberCreated;
import com.whoz_in.main_api.shared.event.Events;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberCreatedEventHandler {

    // Domain 이벤트를 Query 구현 모듈로 전달
    @EventListener(MemberCreated.class)
    public void raiseEvent(MemberCreated event){
        UUID memberId = event.getMember().getId().id();

        Events.raise(new MemberCreatedEvent(memberId));
    }

}
