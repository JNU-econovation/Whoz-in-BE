package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.model.AuthCredentials;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter extends BaseConverter<MemberEntity, Member> {

    @Override
    public MemberEntity from(Member member) {
        AuthCredentials auth = member.getAuthCredentials().orElse(null);
        OAuthCredentials oAuth = member.getOAuthCredentials().orElse(null);
        return new MemberEntity(
                member.getId().id(),
                member.getName(),
                member.getGeneration(),
                member.getMainPosition(),
                member.getStatusMessage(),
                auth != null ? auth.getLoginId() : null,
                auth != null ? auth.getEncodedPassword() : null,
                oAuth != null ? oAuth.getSocialProvider() : null,
                oAuth != null ? oAuth.getSocialId() : null
        );
    }

    @Override
    public Member to(MemberEntity entity) {
        Set<BadgeId> badgeIds = entity.getBadgeMembers().stream()
                .map(badgeMember -> new BadgeId(badgeMember.getBadgeEntity().getId()))
                .collect(Collectors.toSet());
        return Member.load(
                new MemberId(entity.getId()),
                entity.getName(),
                entity.getPosition(),
                entity.getGeneration(),
                entity.getStatusMessage(),
                (entity.getLoginId() != null && entity.getPassword() != null) ?
                        AuthCredentials.load(entity.getLoginId(), entity.getPassword()) : null,
                (entity.getSocialProvider() != null && entity.getSocialId() != null) ?
                        OAuthCredentials.load(entity.getSocialProvider(), entity.getSocialId()) : null,
                badgeIds
        );
    }
}
