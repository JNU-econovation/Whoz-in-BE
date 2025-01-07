package com.whoz_in.domain_jpa.monitor;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {

    List<MonitorLogEntity> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime updatedAt);

}
