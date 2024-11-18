package com.whoz_in.network_log.infrastructure.jpa.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = NetworkLog.PREFIX)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NetworkLog extends BaseEntity {

    public static final String PREFIX = "managed_log";

    @Id // uuid
    @Column(name = PREFIX + "_mac_address", nullable = false, unique = true)
    private String macAddress;

    @Column(name = PREFIX + "_ip_address", nullable = false)
    private String ipAddress;

    @Column(name = PREFIX + "_wifi_ssid", nullable = true)
    private String wifiSsid;

    @Column(name = PREFIX + "_device_name", nullable = true)
    private String deviceName;

    public static NetworkLog create(Map<String, String> log) {
        return NetworkLog.builder()
                .macAddress(log.get("src_mac"))
                .ipAddress(log.get("src_ip"))
                .deviceName(log.get("device_name"))
                .build();
    }

}
