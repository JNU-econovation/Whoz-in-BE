package com.whoz_in.main_api.query.member.application.detail;

import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberDetailHandler implements QueryHandler<MemberDetailGet, MemberDetail> {
    private final MemberInfoViewer memberInfoViewer;
    private final RequesterInfo requesterInfo;

    @Override
    public MemberDetail handle(MemberDetailGet query) {
        MemberInfoView memberInfoView = memberInfoViewer.findByMemberId(requesterInfo.getMemberId().id()).get();
        return new MemberDetail(
                memberInfoView.memberId(),
                memberInfoView.generation(),
                memberInfoView.name(),
                memberInfoView.position(),
                memberInfoView.statusMessage()
        );
    }
}
