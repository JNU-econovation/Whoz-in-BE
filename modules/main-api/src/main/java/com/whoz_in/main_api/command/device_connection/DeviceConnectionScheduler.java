package com.whoz_in.main_api.command.device_connection;

import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.DISCONNECTED_TERM_MINUTE;
import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.UPDATE_TERM_MINUTE;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeviceConnectionScheduler {
    private final DeviceConnectedChecker connectedChecker;
    private final DeviceDisconnectedChecker disconnectedChecker;
    private final MonitorLogStore monitorLogStore;
    private LocalDateTime lastUpdatedAt;

    // 서버 시작 시 캐시된 로그가 없으므로 범위 내로 모두 가져온다.
    @EventListener(ApplicationReadyEvent.class)
    private void init() {
        LocalDateTime since = LocalDateTime.now().minusMinutes(DISCONNECTED_TERM_MINUTE);
        monitorLogStore.updateCache(since);
    }

    @Scheduled(fixedRate = UPDATE_TERM_MINUTE, timeUnit = TimeUnit.MINUTES)
    public void update() {
        lastUpdatedAt = (lastUpdatedAt == null) ?
                LocalDateTime.now() :
                lastUpdatedAt.plusMinutes(UPDATE_TERM_MINUTE);

        LocalDateTime since = lastUpdatedAt.minusMinutes(UPDATE_TERM_MINUTE);
        monitorLogStore.updateCache(since);

        connectedChecker.updateConnected(monitorLogStore.getLogs(since));
        disconnectedChecker.updateDisconnected(monitorLogStore.getActiveMacs());
    }
}
