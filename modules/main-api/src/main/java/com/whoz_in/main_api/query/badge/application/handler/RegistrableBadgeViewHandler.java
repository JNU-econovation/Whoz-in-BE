package com.whoz_in.main_api.query.badge.application.handler;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.query.RegistrableBadgeQuery;
import com.whoz_in.main_api.query.badge.application.view.RegistrableBadges;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class RegistrableBadgeViewHandler implements QueryHandler<RegistrableBadgeQuery, RegistrableBadges> {
    private final BadgeViewer viewer;
    private final RequesterInfo requesterInfo;

    @Override
    public RegistrableBadges handle(RegistrableBadgeQuery query) {
        UUID memberId = requesterInfo.getMemberId().id();
        RegistrableBadges badges = viewer.findRegisterable(memberId);
        return badges;
    }
}
