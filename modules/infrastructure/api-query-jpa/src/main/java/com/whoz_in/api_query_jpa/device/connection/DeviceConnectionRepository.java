package com.whoz_in.api_query_jpa.device.connection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceConnectionRepository extends JpaRepository<DeviceConnection, UUID> {
    List<DeviceConnection> findByConnectedAtBetween(LocalDateTime start, LocalDateTime end);
}
