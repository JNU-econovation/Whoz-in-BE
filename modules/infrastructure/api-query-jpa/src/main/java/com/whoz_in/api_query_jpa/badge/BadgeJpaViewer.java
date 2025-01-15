package com.whoz_in.api_query_jpa.badge;

import com.whoz_in.main_api.query.badge.application.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeJpaViewer implements BadgeViewer {
    private final BadgeRepository repository;

    @Override
    public Optional<BadgeInfo> findBadgeInfoByBadgeId(UUID badgeId) {
        return repository.findById(badgeId)
                .map(badge -> new BadgeInfo(badge.getName(),badge.getColorCode()));
    }
}
