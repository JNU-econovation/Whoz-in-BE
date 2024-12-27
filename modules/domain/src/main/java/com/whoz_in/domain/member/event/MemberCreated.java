package com.whoz_in.domain.member.event;

import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MemberCreated extends DomainEvent {
    private final Member member;
}
