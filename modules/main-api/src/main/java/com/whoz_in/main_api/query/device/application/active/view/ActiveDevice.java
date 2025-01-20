package com.whoz_in.main_api.query.device.application.active.view;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.LocalDateTime;
import java.util.UUID;

// Active 상태인 기기를 나타내는 View
// 접속 시작 시간, 접속 종료 시간
public record ActiveDevice(
    UUID deviceId,
    LocalDateTime connectedTime,
    LocalDateTime disConnectedTime
) implements View {
}
