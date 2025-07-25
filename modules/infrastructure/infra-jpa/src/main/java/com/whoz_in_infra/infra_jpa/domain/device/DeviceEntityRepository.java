package com.whoz_in_infra.infra_jpa.domain.device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceEntityRepository extends JpaRepository<DeviceEntity, UUID> {
    @Query("SELECT d FROM DeviceEntity d JOIN FETCH d.deviceInfoEntity di WHERE di.mac = :mac")
    Optional<DeviceEntity> findByMac(@Param("mac") String mac);

    @Query("SELECT d FROM DeviceEntity d JOIN FETCH d.deviceInfoEntity di WHERE d.id = :deviceId")
    Optional<DeviceEntity> findOneById(@Param("deviceId") UUID deviceId);

    @Query("SELECT d FROM DeviceEntity d JOIN d.deviceInfoEntity di WHERE di.mac IN (:macs)")
    List<DeviceEntity> findByMacs(@Param("macs") List<String> macs);

    @Query("SELECT d FROM DeviceEntity d WHERE d.id IN :deviceIds")
    List<DeviceEntity> findByDeviceIds(@Param("deviceIds") List<UUID> deviceIds);

    List<DeviceEntity> findAllByMemberId(UUID memberId);
}
