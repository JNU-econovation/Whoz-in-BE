package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.exception.NotFoundBadgeException;
import com.whoz_in.domain.badge.model.Badge;
import com.whoz_in.domain.member.event.MemberCreated;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.application.Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class BadgeHandler {
    private final BadgeRepository repository;

    @Transactional
    @EventListener
    public Void handle(MemberCreated event) {
        String position = event.getMember().getMainPosition().getPosition();
        Badge badge = repository.findByName(position).orElseThrow(()-> new NotFoundBadgeException());
        Member member = event.getMember();
        Badge newBadge = Badge.load(
                badge.getId(),
                badge.getBadgeInfo(),
                List.of(new MemberId(member.getId().id()))
        );
        repository.register(newBadge);
        return null;
    }
}
