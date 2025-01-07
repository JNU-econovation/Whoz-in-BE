package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MonitorLogRepository {
    void save(MonitorLog log);
    void saveAll(Collection<MonitorLog> logs);
    List<MonitorLog> findAll();
    List<MonitorLog> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime updatedAt); // 해당 시간 이후의 로그를 가져오기
    // TODO: 이전 로그까지 가져와야 할까?
}
