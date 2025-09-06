package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MemberStatusMessageChanged extends DomainEvent {
    private final String memberId;
    private final String statusMessage;
}
