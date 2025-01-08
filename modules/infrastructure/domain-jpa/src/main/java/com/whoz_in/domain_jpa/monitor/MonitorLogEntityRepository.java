package com.whoz_in.domain_jpa.monitor;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {
    boolean existsByMacAndUpdatedAtAfter(String mac, LocalDateTime time);
}
