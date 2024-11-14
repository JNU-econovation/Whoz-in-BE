package com.whoz_in.networklog.domain.log.model;

import java.time.LocalDateTime;

public record NetworkAddress(String macAddress,
                             String ipAddress,
                             LocalDateTime time,
                             String ssid,
                             boolean isMonitorMode){

    public static NetworkAddress create(String macAddress, String ipAddress, LocalDateTime time, String ssid, boolean isMonitorMode) {
        return new NetworkAddress(macAddress, ipAddress, time, ssid, isMonitorMode);
    }
}
