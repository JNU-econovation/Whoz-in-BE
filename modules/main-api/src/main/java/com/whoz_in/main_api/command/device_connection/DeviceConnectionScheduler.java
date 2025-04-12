package com.whoz_in.main_api.command.device_connection;

import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.DISCONNECTED_TERM_MINUTE;
import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.UPDATE_TERM_MINUTE;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceConnectionScheduler {
    private final DeviceConnectedChecker connectedChecker;
    private final DeviceDisconnectedChecker disconnectedChecker;
    private final MonitorLogRepository monitorLogRepository;

    private final Cache<String, MonitorLog> monitorLogCache = CacheBuilder.newBuilder()
            .expireAfterWrite(DISCONNECTED_TERM_MINUTE, TimeUnit.MINUTES)
            .build();

    @PostConstruct
    private void preloadLogs() {
        // 처음가져올 땐 모두 가져옴
        fetchLogs(DISCONNECTED_TERM_MINUTE);
    }


    public void fetchLogs(int termMinute){
        LocalDateTime since = LocalDateTime.now()
                .minusMinutes(termMinute)
                .truncatedTo(ChronoUnit.MINUTES); // 분 단위로 자르기
        monitorLogRepository.findAllByUpdatedAtAfter(since).forEach(log -> {
            monitorLogCache.asMap().merge(
                    log.getMac(),
                    log,
                    // 같은 맥이 각 다른 방에서 출현했을 경우 최신 log만 저장
                    (existing, incoming) -> incoming.getUpdatedAt().isAfter(existing.getUpdatedAt()) ?
                            incoming : existing
            );
        });
    }

    /*
        monitorlogcache에는 1 ~ 10의 로그가 있고 getNewLogs는 10이 있을때
        updateDisconnected는 '1 ~ 10에 없는' 로그를 가진 디바이스들을 처리하고
        updateConnected는 '10에 있는' 로그를 가진 디바이스들만 처리하니 충돌이 일어날 일은 없음
    */
    @Scheduled(fixedRate = UPDATE_TERM_MINUTE, timeUnit = TimeUnit.MINUTES)
    public void update() {
        fetchLogs(UPDATE_TERM_MINUTE);
        connectedChecker.updateConnected(getNewLogs());
        disconnectedChecker.updateDisconnected(monitorLogCache.asMap().keySet());
    }

    private List<MonitorLog> getNewLogs() {
        LocalDateTime since = LocalDateTime.now().minusMinutes(UPDATE_TERM_MINUTE);
        return monitorLogCache.asMap().values().stream()
                .filter(log -> log.getUpdatedAt().isAfter(since))
                .toList();
    }
}
