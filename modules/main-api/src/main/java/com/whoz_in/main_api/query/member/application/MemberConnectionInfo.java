package com.whoz_in.main_api.query.member.application;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public record MemberConnectionInfo(
        UUID memberId,
        @Nullable Duration dailyTime,
        @Nullable Duration totalTime,
        boolean isActive
    ) implements View {

    public Duration totalTime(){
        if(Objects.isNull(totalTime)) return Duration.ZERO;
        return totalTime;
    }
    
    public Duration dailyTime(){
        if(Objects.isNull(dailyTime)) return Duration.ZERO;
        return dailyTime;
    }

}
