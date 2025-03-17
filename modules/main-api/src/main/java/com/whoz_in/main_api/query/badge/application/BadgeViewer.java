package com.whoz_in.main_api.query.badge.application;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.RegisterableBadges;
import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.Optional;
import java.util.UUID;

public interface BadgeViewer extends Viewer {
    Optional<BadgeInfo> findBadgeInfoByBadgeId(UUID badgeId);

    RegisterableBadges findRegisterable(UUID memberId);

    // 멤버의 뱃지 조회
    BadgesOfMember findBadgesOfMember(UUID memberId);
}
