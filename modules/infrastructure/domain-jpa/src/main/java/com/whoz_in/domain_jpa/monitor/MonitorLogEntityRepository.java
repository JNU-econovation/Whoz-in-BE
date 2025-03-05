package com.whoz_in.domain_jpa.monitor;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {

    @Query("SELECT m FROM MonitorLogEntity m WHERE m.updatedAt > :updatedAt ORDER BY m.updatedAt DESC")
    List<MonitorLogEntity> findByUpdatedAtAfterOrderByUpdatedAtDesc(@Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT 1 FROM MonitorLogEntity m WHERE m.mac = :mac AND m.updatedAt > :time ORDER BY m.updatedAt DESC")
    boolean existsTopByMacOrderByUpdatedAtDescAfter(String mac, LocalDateTime time);
    boolean existsByMacAndUpdatedAtAfter(String mac, LocalDateTime time);
}
