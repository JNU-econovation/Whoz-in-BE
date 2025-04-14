package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.shared.utils.TimeFormatter;
import com.whoz_in.shared.Nullable;
import java.time.Duration;

public record MemberInRoom(
    String memberId,
    int generation,
    String memberName,
    String mainBadgeName,
    String mainBadgeColor,
    String todayActiveTime,
    boolean isActive
) implements Response {
    public MemberInRoom(MemberInfoView info, @Nullable TodayActivityView today) {
        this(
                info.memberId().toString(),
                info.generation(),
                info.name(),
                info.mainBadgeName(),
                info.mainBadgeColor(),
                TimeFormatter.hourMinuteTime(today != null ? today.activeTime() : Duration.ZERO),
                today != null && today.isActive()
        );
    }
}
