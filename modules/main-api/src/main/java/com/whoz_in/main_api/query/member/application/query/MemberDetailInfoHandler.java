package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberDetailInfoHandler implements QueryHandler<MemberDetailInfoGet, MemberDetailInfo> {
    private final MemberViewer memberViewer;
    private final RequesterInfo requesterInfo;

    @Override
    public MemberDetailInfo handle(MemberDetailInfoGet query) {
        return memberViewer.findDetailByMemberId(requesterInfo.getMemberId().id())
                .orElseThrow(()->new IllegalArgumentException("멤버 없음"));
    }
}
