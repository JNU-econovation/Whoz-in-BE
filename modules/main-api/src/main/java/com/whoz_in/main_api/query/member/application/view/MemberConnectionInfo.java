package com.whoz_in.main_api.query.member.application.view;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import org.checkerframework.checker.units.qual.N;

public record MemberConnectionInfo(
        UUID memberId,
        @Nullable Duration dailyTime,
        @Nullable Duration totalTime,
        @Nullable LocalDateTime activeAt,
        @Nullable LocalDateTime inActiveAt,
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

    public Duration continuousTime(){
        if(Objects.isNull(activeAt) && Objects.isNull(inActiveAt)) return Duration.ZERO;
        else if(Objects.isNull(inActiveAt)) return Duration.between(activeAt, LocalDateTime.now());
        return Duration.between(activeAt, inActiveAt).abs();
    }

    public static MemberConnectionInfo empty(UUID memberId){
        return new MemberConnectionInfo(memberId, null, null, null, null, false);
    }

}
