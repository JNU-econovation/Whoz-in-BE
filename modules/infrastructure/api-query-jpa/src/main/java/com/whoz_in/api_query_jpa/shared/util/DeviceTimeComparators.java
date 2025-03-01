package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;


public class DeviceTimeComparators {

    public static Comparator<ActiveDeviceEntity> disConnectedTimeComparator(){
        return (activeDevice1, activeDevice2) -> {
            LocalDateTime ad1DisconnectedAt = activeDevice1.getDisConnectedAt();
            LocalDateTime ad2DisconnectedAt = activeDevice2.getDisConnectedAt();
            if(ad1DisconnectedAt.isEqual(ad2DisconnectedAt)) return 0;
            if(ad1DisconnectedAt.isAfter(ad2DisconnectedAt)) return 1;
            return -1;
        };
    }

    public static Comparator<ActiveDeviceEntity> continuousTimeComparator() {
        return (activeDevice1, activeDevice2) -> {
            Duration ad1continuousTime = Duration.between(activeDevice1.getConnectedAt(),
                    activeDevice1.getDisConnectedAt()).abs();
            Duration ad2continuousTime = Duration.between(activeDevice2.getConnectedAt(),
                    activeDevice2.getDisConnectedAt()).abs();
            return ad1continuousTime.compareTo(ad2continuousTime);
        };
    }

}
