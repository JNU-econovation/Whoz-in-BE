package com.whoz_in.api_query_jpa.badge;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember.BadgeOfMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeJpaViewer implements BadgeViewer {
    private final BadgeRepository bageRepo;
    private final BadgeMemberRepository badgeMemberRepo;

    @Override
    public Optional<BadgeInfo> findBadgeInfoByBadgeId(UUID badgeId) {
        return bageRepo.findById(badgeId)
                .map(badge -> new BadgeInfo(badge.getName(),badge.getColor_code()));
    }

    @Override
    public BadgesOfMember findBadgesOfMember(UUID memberId) {
        List<BadgeMember> badgeMembers = badgeMemberRepo.findByMemberId(memberId);

        List<BadgeOfMember> badgeOfMembers = badgeMembers.stream()
                .map(bm -> new BadgeOfMember(bm.getBadge_id(), bm.getIs_badge_shown())) // getBadge()로 badgeId 가져오기
                .collect(Collectors.toList());

        return new BadgesOfMember(badgeOfMembers);
    }
}
