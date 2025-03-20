package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ManagedLogRepository {
    void saveAll(Collection<ManagedLog> logs);
    Optional<ManagedLog> findBySsidAndMac(String ssid, String mac);
    List<ManagedLog> findAllByIpLatestMac(String ip, LocalDateTime time); // 고정 맥일 경우 같은 아이피가 있을 수 있으므로 List
    default ManagedLog getBySsidAndMac(String ssid, String mac){
        return findBySsidAndMac(ssid, mac).orElseThrow(() -> new NoManagedLogException(mac));
    }
}
