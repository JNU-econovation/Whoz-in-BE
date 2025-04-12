package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MonitorLogRepository {
    void save(MonitorLog log);
    void saveAll(Collection<MonitorLog> logs);
    Optional<MonitorLog> findLatestByMacs(Collection<String> macs);
    Optional<MonitorLog> findLatestByRoomAndMacs(String room, Collection<String> macs);
    List<MonitorLog> findAllByUpdatedAtAfter(LocalDateTime updatedAt); // 해당 시간 이후의 로그를 가져오기
    boolean existsAfter(String mac, LocalDateTime time);
    default void mustExistAfter(String mac, LocalDateTime time){
        if (!existsAfter(mac, time))
            throw new NoMonitorLogException(mac);
    }
}
