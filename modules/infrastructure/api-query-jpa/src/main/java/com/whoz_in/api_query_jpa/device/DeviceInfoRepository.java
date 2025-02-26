package com.whoz_in.api_query_jpa.device;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, Long> {
    @Query(value = "SELECT di.id, di.device_id, di.mac, di.ssid " +
            "FROM device_entity d " +
            "JOIN device_info_entity di ON d.id = di.device_id " +
            "WHERE d.member_id = :ownerId " +
            "AND di.room = :room " +
            "AND di.mac = :mac",
            nativeQuery = true)
    List<DeviceInfo> findAllByMac(@Param("ownerId") UUID ownerId, @Param("room") String room, @Param("mac") String mac);
}
