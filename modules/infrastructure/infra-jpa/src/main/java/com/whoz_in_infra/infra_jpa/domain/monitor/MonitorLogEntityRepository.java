package com.whoz_in_infra.infra_jpa.domain.monitor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorLogEntityRepository extends JpaRepository<MonitorLogEntity, String> {
    Optional<MonitorLogEntity> findTopByLogIdMacInOrderByUpdatedAtDesc(Collection<String> macs);
    Optional<MonitorLogEntity> findTopByLogIdRoomAndLogIdMacInOrderByUpdatedAtDesc(String room, Collection<String> macs);
    List<MonitorLogEntity> findAllByUpdatedAtAfter(LocalDateTime updatedAt);
    boolean existsByLogIdMacAndUpdatedAtAfter(String mac, LocalDateTime time);
}
