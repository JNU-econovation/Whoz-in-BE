package com.whoz_in.api_query_jpa.member.event;

import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.shared.domain_event.member.MemberCreated;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class CreateEmptyConnectionOnMemberCreated {

    private final MemberConnectionInfoRepository connectionInfoRepository;

    @EventListener(MemberCreated.class)
    public void createEmptyConnectionInfo(MemberCreated event){
        UUID memberId = event.getMemberId();

        MemberConnectionInfo newEntity = MemberConnectionInfo.create(memberId);

        connectionInfoRepository.save(newEntity);
    }

}
