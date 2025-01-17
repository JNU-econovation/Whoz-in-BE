package com.whoz_in.domain_jpa.device;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceEntityRepository extends JpaRepository<DeviceEntity, Long> {
    @Query("SELECT d FROM DeviceEntity d JOIN d.deviceInfoEntity di WHERE di.mac = :mac")
    Optional<DeviceEntity> findByMac(@Param("mac") String mac);

    Optional<DeviceEntity> findById(UUID deviceId);

    void deleteById(UUID deviceId);
}