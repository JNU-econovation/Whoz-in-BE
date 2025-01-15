package com.whoz_in.main_api.query.badge.application;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class BadgeInfoViewHandler implements QueryHandler<BadgeId, BadgeInfoResponse> {
    private final BadgeViewer viewr;

    @Override
    public BadgeInfoResponse handle(BadgeId query) {
        BadgeInfo badgeInfo = viewr.findBadgeInfoByBadgeId(query.badgeId()).orElseThrow();
        return new BadgeInfoResponse(badgeInfo.name(), badgeInfo.colorCode());
    }
}
