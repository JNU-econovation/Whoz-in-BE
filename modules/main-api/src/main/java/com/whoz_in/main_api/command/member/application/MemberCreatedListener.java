package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.exception.NotFoundBadgeException;
import com.whoz_in.domain.badge.model.Badge;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.event.MemberCreated;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCreatedListener {
    private final BadgeRepository badgeRepo;
    private final MemberRepository memberRepo;
    private final EventBus eventBus;

    @EventListener
    public Void handleMemberCreatedEvent(MemberCreated event) {
        Member member = event.getMember();
        String position = member.getMainPosition().getPosition();
        Badge badge = badgeRepo.findByName(position).orElseThrow(()-> new NotFoundBadgeException());
        memberRepo.addBadge(member.getId(),badge.getId());
        eventBus.publish(member.pullDomainEvents());
        return null;
    }
}

