package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MemberCreated extends DomainEvent {
    private final UUID memberId;
    private final String name;
    private final String mainPosition;
    private final int generation;
    private final String statusMessage;
    private final String socialProvider;
    private final String socialId;

    private final Map<UUID, Boolean> badges;
    private final UUID mainBadge;
}
