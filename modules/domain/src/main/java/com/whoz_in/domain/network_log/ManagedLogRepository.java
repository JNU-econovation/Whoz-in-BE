package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface ManagedLogRepository {
    void save(ManagedLog log);
    void saveAll(Collection<ManagedLog> logs);
    Optional<ManagedLog> findLatestByIpAfter(String ip, LocalDateTime time);
    Optional<ManagedLog> findBySsidAndMac(String ssid, String mac);
    default ManagedLog getLatestByIpAfter(String ip, LocalDateTime time){
        return findLatestByIpAfter(ip, time).orElseThrow(() -> new IllegalArgumentException("managed 로그가 없습니다"));
    }
    default ManagedLog getBySsidAndMac(String ssid, String mac){
        return findBySsidAndMac(ssid, mac).orElseThrow(() -> new IllegalArgumentException("managed 로그가 없습니다"));
    }
}
