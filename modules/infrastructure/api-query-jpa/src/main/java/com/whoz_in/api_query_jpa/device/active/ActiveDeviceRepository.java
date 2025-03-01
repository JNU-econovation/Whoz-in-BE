package com.whoz_in.api_query_jpa.device.active;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActiveDeviceRepository extends JpaRepository<ActiveDeviceEntity, UUID> {

    @Query("SELECT ad FROM ActiveDeviceEntity ad WHERE ad.deviceId IN (:deviceIds)")
    List<ActiveDeviceEntity> findByDeviceIds(@Param("deviceIds") List<UUID> deviceIds);

    @Query("SELECT ad FROM ActiveDeviceEntity ad WHERE ad.deviceId IN (SELECT d.id FROM Device d WHERE d.memberId=:ownerId)")
    List<ActiveDeviceEntity> findByMemberId(@Param("ownerId") UUID ownerId);

    Optional<ActiveDeviceEntity> findByDeviceId(UUID deviceId);

    void deleteByDeviceId(UUID deviceId);

    boolean existsByDeviceId(UUID deviceId);

    @Query("SELECT ad FROM ActiveDeviceEntity ad WHERE ad.deviceId IN (SELECT d.id FROM Device d WHERE d.memberId=:memberId)")
    List<ActiveDeviceEntity> findMyActiveDeviceEntityByMemberId(@Param("memberId") UUID memberId);
}
