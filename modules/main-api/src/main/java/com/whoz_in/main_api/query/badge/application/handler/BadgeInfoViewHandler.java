package com.whoz_in.main_api.query.badge.application.handler;

import com.whoz_in.domain.badge.exception.NoBadgeException;
import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.query.BadgeInfoGet;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class BadgeInfoViewHandler implements QueryHandler<BadgeInfoGet, BadgeInfo> {
    private final BadgeViewer viewer;

    @Override
    public BadgeInfo handle(BadgeInfoGet query) {
        BadgeInfo badgeInfo = viewer.findBadgeInfoByBadgeId(query.badgeId()).orElseThrow(()-> NoBadgeException.EXCEPTION);
        return badgeInfo;
    }
}

