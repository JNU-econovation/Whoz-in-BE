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
    private final BadgeRepository badgeRepo;
    private final BadgeMemberRepository badgeMemberRepo;

    @Override
    public Optional<BadgeInfo> findBadgeInfoByBadgeId(UUID badgeId) {
        return badgeRepo.findOneById(badgeId)
                .map(badge -> new BadgeInfo(badge.getName(),badge.getColorCode(), badge.getDescription()));
    }

    @Override
    public RegistrableBadges findRegisterable(UUID memberId) {
        /*
        등록가능한 뱃지는 생성된지 12시간이 지나고 개발단 뱃지가 아니어야 함
        그리고 사용자가 이미 가지고 있는 뱃지가 아니어야 한다.
         */
        LocalDateTime threshold = LocalDateTime.now().minusHours(12);
        List<Badge> activeBadges = badgeRepo.findAllActivatedBadges(threshold);
        List<BadgeMember> badgeMembers = badgeMemberRepo.findAllByMemberId(memberId);

        Set<UUID> ownedBadgeIds = badgeMembers.stream()
                .map(BadgeMember::getBadgeId)
                .collect(Collectors.toSet());

        List<RegistrableBadges.RegistrableBadge> registerableBadgeList = activeBadges.stream()
                .filter(badge -> !ownedBadgeIds.contains(badge.getId()))
                .map(badge -> new RegistrableBadges.RegistrableBadge(badge.getId(), badge.getName(), badge.getColorCode(), badge.getDescription()))
                .toList();

        return new RegistrableBadges(registerableBadgeList);
    }

    @Override
    public BadgesOfMember findBadgesOfMember(UUID memberId) {
        List<BadgeMember> badgeMembers = badgeMemberRepo.findAllByMemberId(memberId);

        List<BadgesOfMember.BadgeOfMember> badgeList = badgeMembers.stream()
                .map(bm -> new BadgesOfMember.BadgeOfMember(
                        bm.getBadgeId(),
                        bm.getName(),
                        bm.getColorCode(),
                        bm.getDescription(),
                        bm.getIsBadgeShown()
                ))
                .toList();

        return new BadgesOfMember(badgeList);
    }
}
