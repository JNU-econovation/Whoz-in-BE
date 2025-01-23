package com.whoz_in.main_api.query.device.application.active.view;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

// Active 상태인 기기를 나타내는 View
// 접속 시작 시간, 접속 종료 시간
public record ActiveDevice(
    UUID deviceId,
    LocalDateTime connectedTime,
    @Nullable LocalDateTime disConnectedTime
) implements View {

    public boolean isActive(){
        return Objects.nonNull(connectedTime) && Objects.isNull(disConnectedTime);
    }

    public Duration continuousTime(){
        if(Objects.isNull(connectedTime)) return Duration.ZERO;
        if(Objects.isNull(disConnectedTime)) return Duration.between(connectedTime, LocalDateTime.now()).abs();
        return Duration.between(connectedTime, disConnectedTime);
    }

}
