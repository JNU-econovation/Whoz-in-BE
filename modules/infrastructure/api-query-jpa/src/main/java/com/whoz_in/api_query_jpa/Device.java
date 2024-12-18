package com.whoz_in.api_query_jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Immutable
@Subselect("SELECT "
        + "d.device_id AS deviceId "
        + "FROM device_entity d")
@Synchronize({"device_entity"})
public class Device {
    @Id
    private Long deviceId;

}
