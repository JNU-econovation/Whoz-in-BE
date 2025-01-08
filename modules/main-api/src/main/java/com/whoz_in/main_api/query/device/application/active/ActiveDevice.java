package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

// Active 상태인 기기를 나타내는 View
// 접속 시작 시간, 접속 종료 시간
public record ActiveDevice(
    UUID deviceId,
    UUID memberId,
    LocalDateTime connectedTime,
    LocalDateTime disconnectedTime,
    Duration totalConnectedTime
) implements View {
}
