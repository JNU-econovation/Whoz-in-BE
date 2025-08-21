package com.whoz_in.main_api.command.device_connection;

import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.DISCONNECTED_TERM_MINUTE;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 기기의 연결 및 연결 끊김 여부는 모니터 로그로 판단하는데, 매번 새로 가져오는 것보다 필요한만큼 캐싱해두는 것이 낫다고 판단함.
 {@link com.whoz_in.main_api.command.device_connection.DeviceConnectionScheduler}에서만 쓸 생각으로 만든 것임
 */
@Component
@RequiredArgsConstructor
public class MonitorLogStore {
    private final MonitorLogRepository monitorLogRepository;

    // Cache<Mac, MonitorLog>
    private final Cache<String, MonitorLog> monitorLogCache = CacheBuilder.newBuilder()
            .expireAfterWrite(DISCONNECTED_TERM_MINUTE, TimeUnit.MINUTES)
            .build();


    @Transactional(readOnly = true)
    public void updateCache(LocalDateTime since) {
        monitorLogRepository.findAllByUpdatedAtAfter(since).forEach(log -> {
            monitorLogCache.asMap().merge(
                    log.getMac(),
                    log,
                    (existing, incoming) -> incoming.getUpdatedAt().isAfter(existing.getUpdatedAt()) ? incoming : existing
            );
        });
    }

    public List<MonitorLog> getLogs(LocalDateTime since) {
        return monitorLogCache.asMap().values().stream()
                .filter(log -> log.getUpdatedAt().isAfter(since))
                .toList();
    }

    public Set<String> getActiveMacs() {
        return monitorLogCache.asMap().keySet();
    }
}
