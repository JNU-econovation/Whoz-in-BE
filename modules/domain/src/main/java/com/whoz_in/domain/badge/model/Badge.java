package com.whoz_in.domain.badge.model;

import com.whoz_in.domain.badge.event.BadgeCreated;
import com.whoz_in.domain.shared.AggregateRoot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public final class Badge extends AggregateRoot {
    private final BadgeId id;
    private final BadgeInfo badgeInfo;

    public static Badge create(BadgeInfo badgeInfo) {
        Badge badge = Badge.builder()
                .id(new BadgeId())
                .badgeInfo(badgeInfo)
                .build();
        badge.register(new BadgeCreated(badge.getBadgeInfo().getName(),
                badge.getBadgeInfo().getBadgeType().toString(),
                badge.getBadgeInfo().getCreator().id().toString(),
                badge.getBadgeInfo().getColorCode(),
                badge.getBadgeInfo().getDescription()));
        return badge;
    }

    public static Badge load(BadgeId id, BadgeInfo badgeInfo) {
        return Badge.builder()
                .id(id)
                .badgeInfo(badgeInfo)
                .build();
    }
}
