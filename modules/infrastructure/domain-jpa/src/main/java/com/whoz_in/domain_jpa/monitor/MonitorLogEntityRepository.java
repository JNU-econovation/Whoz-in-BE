package com.whoz_in.domain_jpa.monitor;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {

    List<MonitorLogEntity> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime updatedAt);

    @Query("SELECT 1 FROM MonitorLogEntity m WHERE m.mac = :mac AND m.updatedAt > :time ORDER BY m.updatedAt DESC")
    boolean existsTopByMacOrderByUpdatedAtDescAfter(String mac, LocalDateTime time);
    boolean existsByMacAndUpdatedAtAfter(String mac, LocalDateTime time);
}
