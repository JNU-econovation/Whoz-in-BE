package com.whoz_in.api_query_jpa.member.event;

import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.main_api.shared.domain.member.event.MemberCreatedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCreatedEventHandler {

    private final MemberConnectionInfoRepository connectionInfoRepository;

    @EventListener(MemberCreatedEvent.class)
    public void createEmptyConnectionInfo(MemberCreatedEvent event){
        UUID memberId = event.memberId();

        MemberConnectionInfo newEntity = MemberConnectionInfo.create(memberId);

        connectionInfoRepository.save(newEntity);
    }

}
