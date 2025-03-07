package com.whoz_in.main_api.query.badge.application.handler;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.query.EmptyMemberBadgeQuery;
import com.whoz_in.main_api.query.badge.application.response.RegisterableBadgeResponse;
import com.whoz_in.main_api.query.badge.application.view.BadgeIds;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class BadgeIdsOfMemberViewHandler implements QueryHandler<EmptyMemberBadgeQuery, RegisterableBadgeResponse> {
    private final BadgeViewer viewer;
    private final RequesterInfo requesterInfo;

    @Override
    public RegisterableBadgeResponse handle(EmptyMemberBadgeQuery query) {
        UUID memberId = requesterInfo.getMemberId().id();
        BadgeIds badgeIds = viewer.findBadgesByMemberId(memberId);
        return new RegisterableBadgeResponse(badgeIds.badgeIds());
    }
}
