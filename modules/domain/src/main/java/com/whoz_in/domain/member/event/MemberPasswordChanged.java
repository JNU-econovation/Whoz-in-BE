package com.whoz_in.domain.member.event;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MemberPasswordChanged extends DomainEvent {
    private final MemberId memberId;
    private final String newPassword;
}
