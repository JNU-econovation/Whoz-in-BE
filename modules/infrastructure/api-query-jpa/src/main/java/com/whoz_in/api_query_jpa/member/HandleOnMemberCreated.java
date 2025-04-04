package com.whoz_in.api_query_jpa.member;

import com.whoz_in.shared.domain_event.member.MemberCreated;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class HandleOnMemberCreated {
    private final MemberConnectionInfoRepository connectionInfoRepository;

    @TransactionalEventListener(MemberCreated.class)
    public void createEmptyConnectionInfo(MemberCreated event){
        UUID memberId = event.getMemberId();

        MemberConnectionInfo newEntity = MemberConnectionInfo.create(memberId);

        connectionInfoRepository.save(newEntity);
    }

}
