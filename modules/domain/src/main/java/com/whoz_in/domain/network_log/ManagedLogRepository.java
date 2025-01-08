package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface ManagedLogRepository {
    void save(ManagedLog log);
    void saveAll(Collection<ManagedLog> logs);
    Optional<ManagedLog> findLatestByRoomAndIpAfter(String room, String ip, LocalDateTime time);
    default ManagedLog getLatestByRoomAndIpAfter(String room, String ip, LocalDateTime time){
        return findLatestByRoomAndIpAfter(room, ip, time).orElseThrow(() -> new IllegalArgumentException("로그가 없습니다"));
    }
}
