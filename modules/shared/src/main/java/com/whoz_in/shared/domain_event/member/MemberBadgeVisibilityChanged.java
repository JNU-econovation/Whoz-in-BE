package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBadgeVisibilityChanged extends DomainEvent {
    private final String memberId;
    private final String badgeId;
    private final Boolean isVisible;
}
