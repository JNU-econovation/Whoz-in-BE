package com.whoz_in.api_query_jpa.device;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByMemberId(UUID ownerId);
}
