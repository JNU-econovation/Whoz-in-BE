package com.whoz_in_infra.infra_jpa.domain.device_connection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceConnectionEntityRepository extends JpaRepository<DeviceConnectionEntity, UUID> {
    Optional<DeviceConnectionEntity> findByDeviceIdAndDisconnectedAtIsNull(UUID deviceId);
    List<DeviceConnectionEntity> findByDisconnectedAtIsNull();
}
