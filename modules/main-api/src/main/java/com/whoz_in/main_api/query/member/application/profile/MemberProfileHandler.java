package com.whoz_in.main_api.query.member.application.profile;

import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.TimeFormatter;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberProfileHandler implements QueryHandler<MemberProfileGet, MemberProfile> {

    private final MemberInfoViewer memberInfoViewer;

    @Override
    public MemberProfile handle(MemberProfileGet query) {
        MemberInfoView memberInfoView = memberInfoViewer.findByMemberId(query.memberId()).get();
        return new MemberProfile(
                memberInfoView.memberId(),
                memberInfoView.generation(),
                memberInfoView.name(),
                memberInfoView.position(),
                TimeFormatter.dayHourMinuteTime(memberInfoView.totalActiveTime())
        );
    }
}
