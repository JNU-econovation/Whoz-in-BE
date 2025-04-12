package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain_jpa.badge.BadgeMemberEntity;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter extends BaseConverter<MemberEntity, Member> {

    @Override
    public MemberEntity from(Member member) {
        OAuthCredentials oAuth = member.getOAuthCredentials();
        Set<BadgeMemberEntity> badgeMembers = member.getBadges().entrySet().stream()
                .map(entry -> new BadgeMemberEntity(
                        member.getId().id(),
                        entry.getKey().id(),
                        entry.getValue()
                ))
                .collect(Collectors.toSet());
        BadgeId mainBadge = member.getMainBadge();
        return new MemberEntity(
                member.getId().id(),
                member.getName(),
                member.getGeneration(),
                member.getMainPosition(),
                member.getStatusMessage(),
                oAuth.getSocialProvider(),
                oAuth.getSocialId(),
                mainBadge != null ? member.getMainBadge().id() : null,
                badgeMembers
        );
    }

    @Override
    public Member to(MemberEntity entity) {
        Map<BadgeId, Boolean> badges = entity.getBadgeMembers().stream()
                .collect(Collectors.toMap(
                        badgeMember -> new BadgeId(badgeMember.getBadgeId()),
                        BadgeMemberEntity::getIsBadgeShown
                ));
        return Member.load(
                new MemberId(entity.getId()),
                entity.getName(),
                entity.getPosition(),
                entity.getGeneration(),
                entity.getStatusMessage(),
                OAuthCredentials.create(entity.getSocialProvider(), entity.getSocialId()),
                badges,
                new BadgeId(entity.getMainBadge())
        );
    }
}
