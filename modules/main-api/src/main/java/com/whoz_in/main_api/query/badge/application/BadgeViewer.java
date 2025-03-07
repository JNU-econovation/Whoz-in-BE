package com.whoz_in.main_api.query.badge.application;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.Optional;
import java.util.UUID;

public interface BadgeViewer extends Viewer {
    Optional<BadgeInfo> findBadgeInfoByBadgeId(UUID badgeId);
//    BadgeIds findBadgesByMemberId(UUID memberId);
//    BadgeIds findRegisterableBadges(UUID memberId);


//    // 등록 가능한 뱃지 조회
//    List<BadgeId> findRegisterableBadges(UUID memebrId);

    // 멤버의 뱃지 조회
    BadgesOfMember findBadgesOfMember(UUID memberId);
}
