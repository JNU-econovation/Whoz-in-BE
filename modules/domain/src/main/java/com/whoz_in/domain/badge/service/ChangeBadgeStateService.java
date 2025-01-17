package com.whoz_in.domain.badge.service;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ChangeBadgeStateService {
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    public void changeBadgeShowOrHide(MemberId memberId, BadgeId badgeId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow();
        member.changeBadgeShowOrHide(badgeId);

    }
}
