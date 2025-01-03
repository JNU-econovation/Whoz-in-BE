package com.whoz_in.domain.network_log;

import java.util.Collection;

public interface ManagedLogRepository {
    void save(ManagedLog log);
    void saveAll(Collection<ManagedLog> logs);
}
