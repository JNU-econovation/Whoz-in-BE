package com.whoz_in.domain.network_log;

import java.util.Collection;
import java.util.Optional;

public interface ManagedLogRepository {
    void save(ManagedLog log);
    void saveAll(Collection<ManagedLog> logs);
    Optional<ManagedLog> findLatestByRoomAndIp(String room, String ip);
}
