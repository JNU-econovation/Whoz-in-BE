package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

// NI 상태가 올바르지 않을때 5분 후에 에러 로깅 -> 디스코드로 가게 됨
@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class NetworkInterfaceAlertNotifier {
    private final NetworkInterfaceManager networkInterfaceManager;
    @Qualifier("threadPoolTaskScheduler")
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> alertTask;

    @EventListener(NetworkInterfaceStatusEvent.class)
    private void handleTimer(){
        if (!networkInterfaceManager.isAvailable()) {
            if (alertTask != null) return; // 예약 돼있으면 중복 예약 방지

            alertTask = taskScheduler.schedule(
                    () -> {
                        log.error("‼️네트워크 인터페이스가 자동 복구되지 않았습니다. 직접 확인이 필요합니다‼️");
                        alertTask = null;
                    },
                    Instant.now().plus(5, ChronoUnit.MINUTES)
            );
            log.info("연결 끊김 감지: 5분 뒤 에러 알림 예약 완료");
        } else {
            if (alertTask == null) return; // 예약 안 돼있으면 중복 취소 방지

            alertTask.cancel(false);
            alertTask = null;
            log.info("연결 복구 감지: 예약된 에러 알림 취소");
        }
    }
}
