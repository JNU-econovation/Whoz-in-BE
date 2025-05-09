package com.whoz_in.main_api.query.member.application.in_room;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.HourMinuteSerializer;
import com.whoz_in.shared.Nullable;
import java.time.Duration;

public record MemberInRoom(
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
    public MemberInRoom(MemberInfoView info, @Nullable TodayActivityView todayActivity) {
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
