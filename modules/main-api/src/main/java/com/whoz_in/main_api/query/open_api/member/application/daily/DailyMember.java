package com.whoz_in.main_api.query.open_api.member.application.daily;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.open_api.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.HourMinuteSerializer;
import java.time.Duration;

public record DailyMember(
    String memberId,
    int generation,
    String memberName,
    @JsonSerialize(using = HourMinuteSerializer.class)
    Duration todayActiveTime
) implements Response {
    public DailyMember(MemberInfoView info, TodayActivityView todayActivity) {
        this(
                info.memberId().toString(),
                info.generation(),
                info.name(),
                todayActivity.activeTime()
        );
    }
}
