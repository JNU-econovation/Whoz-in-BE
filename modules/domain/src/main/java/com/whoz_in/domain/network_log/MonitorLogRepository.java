package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MonitorLogRepository {
    void save(MonitorLog log);
    void saveAll(Collection<MonitorLog> logs);
    boolean existsAfter(String mac, LocalDateTime time);
    default void mustExistAfter(String mac, LocalDateTime time){
        if (!existsAfter(mac, time))
            throw new IllegalArgumentException("monitor log가 없습니다");
    }
}
