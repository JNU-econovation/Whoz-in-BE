package com.whoz_in.domain.member.event;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.event.DomainEvent;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBadgeAdded extends DomainEvent {
    private final MemberId memberId;
    private final Set<BadgeId> badges;
}
