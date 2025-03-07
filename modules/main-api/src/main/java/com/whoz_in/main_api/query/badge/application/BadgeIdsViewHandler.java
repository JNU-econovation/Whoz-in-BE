package com.whoz_in.main_api.query.badge.application;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class BadgeIdsViewHandler implements QueryHandler<EmptyRegisterableBadgeQuery, RegisterableBadgeResponse> {
    private final BadgeViewer viewer;
    private final RequesterInfo requesterInfo;

    @Override
    public RegisterableBadgeResponse handle(EmptyRegisterableBadgeQuery query) {
        UUID memberId = requesterInfo.getMemberId().id();
        BadgeIds badgeIds = viewer.findRegisterableBadges(memberId);
        return new RegisterableBadgeResponse(badgeIds.badgeIds());
    }
}
