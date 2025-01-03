package com.whoz_in.domain_jpa.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MonitorLogJpaRepository implements MonitorLogRepository {
    private final MonitorLogEntityRepository repository;
    private final MonitorLogConverter converter;

    @Override
    public void save(MonitorLog log) {
        repository.save(converter.from(log));
    }
}
