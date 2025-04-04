package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 이벤트에 비밀번호 담지 않음
@Getter
@RequiredArgsConstructor
public final class MemberPasswordChanged extends DomainEvent {
    private final UUID memberId;
}
