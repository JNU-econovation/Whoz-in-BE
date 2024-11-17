package com.whoz_in.networklog.infrastructure.jpa.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class NetworkLog {

    public static final String PREFIX = "network_information";

    @Id // uuid
    @Column(name = PREFIX + "_id")
    private String id;

    @Column(name = PREFIX + "_mac_address")
    private String macAddress;

    @Column(name = PREFIX + "_ip_address")
    private String ipAddress;

    @Column(name = PREFIX + "_wifi_ssid")
    private String wifiSsid;

    @Column(name = PREFIX + "_device_name")
    private String deviceName;

}
