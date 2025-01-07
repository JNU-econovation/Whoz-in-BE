package com.whoz_in.domain.badge.model;

import com.whoz_in.domain.badge.event.BadgeCreated;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.AggregateRoot;
import java.util.List;
import java.util.UUID;
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
    private final List<MemberId> owners;

    public static Badge create(BadgeInfo badgeInfo, List<MemberId> owners) {
        Badge badge = Badge.builder()
                .id(new BadgeId(UUID.randomUUID()))
                .badgeInfo(badgeInfo)
                .owners(owners)
                .build();
        badge.register(new BadgeCreated());
        return badge;
    }

    public static Badge load(BadgeId id, BadgeInfo badgeInfo, List<MemberId> owners) {
        return Badge.builder()
                .id(id)
                .badgeInfo(badgeInfo)
                .owners(owners)
                .build();
    }
}
