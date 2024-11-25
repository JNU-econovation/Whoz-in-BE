package com.whoz_in.network_log.infra.managed.arp;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArpLog {

    @EqualsAndHashCode.Include
    private String mac;
    @EqualsAndHashCode.Include
    private String ip;

    private String device;
    private String ssid;
    private LocalDateTime createdAt;

    public ArpLog(String mac, String ip, @Nullable String device, String ssid) {
        this.mac = mac;
        this.ip = ip;
        this.device = device;
        this.ssid = ssid;
        this.createdAt = LocalDateTime.now();
    }

}
