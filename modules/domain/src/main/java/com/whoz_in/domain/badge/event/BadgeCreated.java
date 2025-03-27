package com.whoz_in.domain.badge.event;

import com.whoz_in.domain.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BadgeCreated extends DomainEvent {
    private final String name;
    private final String badgeType;
    private final String creatorId;
    private final String colorCode;
    private final String description;
}
