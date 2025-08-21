package com.whoz_in_infra.infra_jpa.query.device;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findOneById(UUID deviceId);
    List<Device> findAllByMemberId(UUID ownerId);
    List<Device> findAllByIdIn(Collection<UUID> deviceIds);
}
