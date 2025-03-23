package com.whoz_in.api_query_jpa.badge;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember.BadgeOfMember;
import com.whoz_in.main_api.query.badge.application.view.RegistrableBadges;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public RegistrableBadges findRegisterable(UUID memberId) {
        LocalDateTime threshold = LocalDateTime.now().minusHours(12);
        List<Badge> activeBadges = bageRepo.findAllActivatedBadges(threshold);
        List<BadgeMember> badgeMembers = badgeMemberRepo.findByMemberId(memberId);

        Set<UUID> ownedBadgeIds = badgeMembers.stream()
                .map(BadgeMember::getBadge_id)
                .collect(Collectors.toSet());

        List<RegistrableBadges.RegistrableBadge> registerableBadgeList = activeBadges.stream()
                .filter(badge -> !ownedBadgeIds.contains(badge.getId()))
                .map(badge -> new RegistrableBadges.RegistrableBadge(badge.getId()))
                .toList();

        return new RegistrableBadges(registerableBadgeList);
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
