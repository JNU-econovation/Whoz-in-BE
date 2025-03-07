package com.whoz_in.main_api.query.badge.application.handler;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.query.EmptyMemberBadgeQuery;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class BadgeOfMemberViewHandler implements QueryHandler<EmptyMemberBadgeQuery, BadgesOfMember> {
    private final BadgeViewer viewer;
    private final RequesterInfo requesterInfo;

    @Override
    public BadgesOfMember handle(EmptyMemberBadgeQuery query) {
        UUID memberId = requesterInfo.getMemberId().id();
        BadgesOfMember badges = viewer.findBadgesOfMember(memberId);
        return badges;
    }
}
