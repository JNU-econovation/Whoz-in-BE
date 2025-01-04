package com.whoz_in.domain_jpa.monitor;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {
    @Query("SELECT 1 FROM MonitorLogEntity m WHERE m.mac = :mac AND m.updatedAt > :time ORDER BY m.updatedAt DESC")
    boolean existsTopByMacOrderByUpdatedAtDescAfter(String mac, LocalDateTime time);
}
