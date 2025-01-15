package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Getter
@Subselect("SELECT di.id, di.device_id, di.mac, di.ssid "
        + "FROM device_info_entity di")
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceInfo {

    @Id
    private Long id;

    private UUID deviceId;

    private String mac;

    private String ssid;
}