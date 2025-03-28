package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.Position;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.member.application.response.MemberProfileInfo;
import com.whoz_in.main_api.query.member.application.support.ConnectionTimeFormatter;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberProfileHandler implements QueryHandler<MemberProfile, MemberProfileInfo> {

    private final MemberViewer memberViewer;
    private final MemberFinderService memberFinderService;

    @Override
    public MemberProfileInfo handle(MemberProfile query) {
        UUID memberId = UUID.fromString(query.memberId());

        memberFinderService.mustExist(new MemberId(memberId));

        MemberConnectionInfo connectionInfo = memberViewer.findConnectionInfo(memberId.toString()).get();
        MemberInfo memberInfo = memberViewer.findNameByMemberId(memberId.toString()).get();

        Duration duration = connectionInfo.totalTime();

        return new MemberProfileInfo(
                memberInfo.memberId().toString(),
                memberInfo.generation(),
                memberInfo.memberName(),
                Position.findByName(memberInfo.position()),
                timeFormat(duration)
        );

    }

    private String timeFormat(Duration duration){
        return ConnectionTimeFormatter.dayHourMinuteTime(duration);
    }
}
