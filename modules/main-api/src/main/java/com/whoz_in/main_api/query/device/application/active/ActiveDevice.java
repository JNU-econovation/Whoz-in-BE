package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

// Active 상태인 기기를 나타내는 View
// 접속 시작 시간, 접속 종료 시간
public record ActiveDevice(
    UUID deviceId,
    UUID memberId,
    LocalDateTime connectedTime,
    LocalDateTime disConnectedTime,
    Duration totalConnectedTime,
    boolean isActive
) implements View {

    // TODO: ActiveTime Calculator 로 이동
    public Duration totalConnectedTime(){
        if(isActive && Objects.nonNull(totalConnectedTime)){ // 한 번 이상 접속을 끊었다 연결한 경우
            return totalConnectedTime.plus(continuousTime());
        } else if (isActive){ // 처음 접속했는데, 안 끊은 경우
            return continuousTime();
        }
        return totalConnectedTime; // 접속을 종료한 경우
    }

    public Duration continuousTime(){
        if(isActive) return Duration.between(connectedTime, LocalDateTime.now()).abs();
        return Duration.between(connectedTime,disConnectedTime).abs();
    }

}
