package com.whoz_in.main_api.query.member.application.profile;

import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberProfileHandler implements QueryHandler<MemberProfileGet, MemberProfile> {

    private final MemberInfoViewer memberInfoViewer;

    @Override
    public MemberProfile handle(MemberProfileGet query) {
        MemberInfoView memberInfoView = memberInfoViewer.findByMemberId(query.memberId())
                .orElseThrow(()-> new IllegalStateException("회원 정보 없음: " + query.memberId()));
        return new MemberProfile(
                memberInfoView.memberId(),
                memberInfoView.generation(),
                memberInfoView.name(),
                memberInfoView.position(),
                memberInfoView.totalActiveTime()
        );
    }
}
