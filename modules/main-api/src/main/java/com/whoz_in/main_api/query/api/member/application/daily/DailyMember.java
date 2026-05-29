package com.whoz_in.main_api.query.api.member.application.daily;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.api.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.api.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.HourMinuteSerializer;
import com.whoz_in.shared.Nullable;
import java.time.Duration;

public record DailyMember(
    String memberId,
    int generation,
    String memberName,
    String mainBadgeName,
    String mainBadgeColor,
    boolean hasBeenActive,
    @JsonSerialize(using = HourMinuteSerializer.class)
    Duration todayActiveTime,
    boolean isActive
) implements Response {
    public DailyMember(MemberInfoView info, @Nullable TodayActivityView todayActivity) {
        this(
                info.memberId().toString(),
                info.generation(),
                info.name(),
                info.mainBadgeName(),
                info.mainBadgeColor(),
                todayActivity != null,
                todayActivity != null ? todayActivity.activeTime() : Duration.ZERO,
                todayActivity != null && todayActivity.isActive()
        );
    }
}
