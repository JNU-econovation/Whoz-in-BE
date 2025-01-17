package com.whoz_in.api_query_jpa.device;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveDeviceRepository extends JpaRepository<ActiveDeviceEntity, UUID> {

    Optional<ActiveDeviceEntity> findByDeviceId(UUID deviceId);

    void deleteByDeviceId(UUID deviceId);

}
