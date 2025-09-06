package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class MainBadgeChangeHandler implements CommandHandler<MainBadgeChange, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberRepository repository;

    @Transactional
    @Override
    public Void handle(MainBadgeChange req) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = repository.findByMemberId(requesterId).orElseThrow(()-> NoMemberException.EXCEPTION);
        member.changeMainBadge(new BadgeId(req.badgeId()));
        repository.save(member);
        return null;
    }
}
