package com.whoz_in.api_query_jpa.monitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MonitorLogRepository extends JpaRepository<MonitorLog, String> {

    @Query("SELECT m FROM MonitorLog m WHERE m.mac = :mac")
    MonitorLog findByMac(String mac);

    @Query("SELECT m FROM MonitorLog m WHERE m.mac = :mac ORDER BY m.updatedAt DESC LIMIT 1")
    MonitorLog findLatestByMac(String mac);

}
