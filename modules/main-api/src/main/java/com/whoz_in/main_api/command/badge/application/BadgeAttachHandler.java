package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.badge.service.BadgeOwnershipService;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class BadgeAttachHandler implements CommandHandler<BadgeAttach, Void> {
    private final MemberRepository repository;
    private final EventBus eventBus;
    private final BadgeOwnershipService badgeOwnershipService;

    @Transactional
    @Override
    public Void handle(BadgeAttach req) {
        Member member = repository.findByMemberId(new MemberId(req.memberId())).orElseThrow();
        badgeOwnershipService.validateType(new BadgeId(req.badgeId()));
        member.addBadge(new BadgeId(req.badgeId()));
        eventBus.publish(member.pullDomainEvents());
        return null;
    }
}
