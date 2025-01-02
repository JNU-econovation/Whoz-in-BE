package com.whoz_in.domain.member.event;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberOAuthInfoRegisteredEventHandler {

    private final MemberRepository memberRepository;

    @EventListener(MemberOAuthInfoRegistered.class)
    @Async
    public void on(MemberOAuthInfoRegistered event) {
        Member member = memberRepository.getByMemberId(event.getAggregateId());

        Member oAuthMember = Member.load(
                member.getId(),
                member.getName(),
                member.getMainPosition(),
                member.getGeneration(),
                member.getStatusMessage(),
                member.getAuthCredentials()
                        .orElse(null),
                event.getOAuthCredentials()
        );

        memberRepository.save(oAuthMember);
    }

}
