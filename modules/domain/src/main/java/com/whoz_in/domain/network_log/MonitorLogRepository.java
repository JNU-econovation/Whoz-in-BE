package com.whoz_in.domain.network_log;

import java.util.Collection;

public interface MonitorLogRepository {
    void save(MonitorLog log);
    void saveAll(Collection<MonitorLog> logs);
}
