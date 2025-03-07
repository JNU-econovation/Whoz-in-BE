package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.domain.badge.model.Badge;
import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.badge.service.BadgeFinderService;
import com.whoz_in.domain.badge.service.BadgeOwnershipService;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class BadgeAttachHandler implements CommandHandler<BadgeAttach, Void> {
    private final EventBus eventBus;
    private final BadgeOwnershipService badgeOwnershipService;
    private final MemberFinderService memberFinderService;
    private final BadgeFinderService badgeFinderService;
    private final RequesterInfo requesterInfo;
    private final MemberRepository repository;

    @Transactional
    @Override
    public Void handle(BadgeAttach req) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = memberFinderService.find(requesterId);
        Badge badge = badgeFinderService.find(new BadgeId(req.badgeId()));
        badgeOwnershipService.validateType(badge.getBadgeInfo().getBadgeType());
        member.addBadge(new BadgeId(req.badgeId()));
        repository.save(member);
        eventBus.publish(member.pullDomainEvents());
        return null;
    }
}
