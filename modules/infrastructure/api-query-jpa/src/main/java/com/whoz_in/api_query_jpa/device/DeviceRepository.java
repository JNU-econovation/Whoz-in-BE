package com.whoz_in.api_query_jpa.device;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    @Query(nativeQuery = true,
            value = "SELECT id , member_id FROM device_entity")
    List<Device> findAll();

}
