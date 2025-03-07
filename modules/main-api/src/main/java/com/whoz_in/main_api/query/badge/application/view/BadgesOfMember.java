package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;
import java.util.List;

public record BadgesOfMember(List<BadgeOfMember> badgeMembers)
        implements Response, View {
    public record BadgeOfMember(
            UUID badgeId,
            Boolean isBadgeShown
    ) {}
}
