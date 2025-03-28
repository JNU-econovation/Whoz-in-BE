package com.whoz_in.main_api.shared.domain.member;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain.member.model.Position;
import com.whoz_in.domain.member.model.SocialProvider;

public class MemberFixture {

    private static final OAuthCredentials testOAuthCredentials = OAuthCredentials.create(SocialProvider.KAKAO, "testSocialId");
    private static final Position testPosition = Position.BE;
    private static final String testName = "testUser";
    private static final BadgeId testMainBadge = new BadgeId();

    public static Member testMember(){
        return Member.create(
                testName,
                testPosition,
                26,
                testOAuthCredentials,
                testMainBadge
        );
    }

}
