package com.whoz_in.main_api.query.badge.application.handler;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.query.RegisterableBadgeQuery;
import com.whoz_in.main_api.query.badge.application.view.RegisterableBadges;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class RegisterableBadgeViewHandler implements QueryHandler<RegisterableBadgeQuery, RegisterableBadges> {
    private final BadgeViewer viewer;
    private final RequesterInfo requesterInfo;

    @Override
    public RegisterableBadges handle(RegisterableBadgeQuery query) {
        UUID memberId = requesterInfo.getMemberId().id();
        RegisterableBadges badges = viewer.findRegisterable(memberId);
        return badges;
    }
}
