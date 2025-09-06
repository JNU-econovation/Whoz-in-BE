package com.whoz_in.main_api.query.member.application.profile;

import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Handler
@RequiredArgsConstructor
@Slf4j
public class MemberProfileHandler implements QueryHandler<MemberProfileGet, MemberProfile> {

    private final MemberInfoViewer memberInfoViewer;

    @Override
    public MemberProfile handle(MemberProfileGet query) {
        MemberInfoView memberInfoView = memberInfoViewer.findByMemberId(query.memberId())
                .orElseThrow(() -> {
                    log.warn("회원 정보 없음: memberId={}", query.memberId());
                    return NoMemberException.EXCEPTION;
                });
        return new MemberProfile(
                memberInfoView.memberId(),
                memberInfoView.generation(),
                memberInfoView.name(),
                memberInfoView.position(),
                memberInfoView.totalActiveTime()
        );
    }
}
