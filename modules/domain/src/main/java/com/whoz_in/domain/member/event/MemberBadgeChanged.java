package com.whoz_in.domain.member.event;

import com.whoz_in.domain.shared.event.DomainEvent;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBadgeChanged extends DomainEvent {
    private final String memberId;
    private final Map<String, Boolean> badges;
}
