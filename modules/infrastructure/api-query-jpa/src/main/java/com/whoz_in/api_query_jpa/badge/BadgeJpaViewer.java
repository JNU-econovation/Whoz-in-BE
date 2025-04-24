package com.whoz_in.api_query_jpa.badge;

import com.whoz_in.main_api.query.badge.application.BadgeViewer;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
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
                .map(badge -> new BadgeInfo(badge.getName(),badge.getColorString(), badge.getDescription()));
    }

    @Override
    public RegistrableBadges findRegisterable(UUID memberId) {
        /*
        등록가능한 뱃지는 생성된지 12시간이 지나고 개발단 뱃지가 아니어야 함
        그리고 사용자가 이미 가지고 있는 뱃지가 아니어야 한다.
         */
        LocalDateTime threshold = LocalDateTime.now().minusHours(12);
        List<Badge> activeBadges = bageRepo.findAllActivatedBadges(threshold);
        List<BadgeMember> badgeMembers = badgeMemberRepo.findByMemberId(memberId);

        Set<UUID> ownedBadgeIds = badgeMembers.stream()
                .map(BadgeMember::getBadgeId)
                .collect(Collectors.toSet());

        List<RegistrableBadges.RegistrableBadge> registerableBadgeList = activeBadges.stream()
                .filter(badge -> !ownedBadgeIds.contains(badge.getId()))
                .map(badge -> new RegistrableBadges.RegistrableBadge(badge.getId(), badge.getName(), badge.getColorString(), badge.getDescription()))
                .toList();

        return new RegistrableBadges(registerableBadgeList);
    }

    @Override
    public BadgesOfMember findBadgesOfMember(UUID memberId) {
        List<BadgeMember> badgeMembers = badgeMemberRepo.findByMemberId(memberId);

        List<BadgesOfMember.BadgeOfMember> badgeList = badgeMembers.stream()
                .map(bm -> new BadgesOfMember.BadgeOfMember(
                        bm.getBadgeId(),
                        bm.getName(),
                        bm.getColorString(),
                        bm.getDescription(),
                        bm.getIsBadgeShown()
                ))
                .toList();

        return new BadgesOfMember(badgeList);
    }

    @Override
    public BadgeInfo findRepresentativeBadge(UUID memberId) {
        Optional<Badge> badge = bageRepo.findRepresentativeBadge(memberId);
        return badge
                .map(b -> new BadgeInfo(b.getName(), b.getColorString(), b.getDescription()))
                .orElseThrow(() -> new IllegalArgumentException("Representative badge not found"));
    }
}
