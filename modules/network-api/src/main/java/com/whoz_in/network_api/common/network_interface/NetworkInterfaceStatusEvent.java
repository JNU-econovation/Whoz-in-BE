package com.whoz_in.network_api.common.network_interface;

public record NetworkInterfaceStatusEvent(
        String interfaceName,
        NetworkInterface pre,
        NetworkInterface now,
        Status status
) {
    public enum Status {
        ADDED, // pre는 null
        RECONNECTED,
        ADDED_AND_RECONNECTED, // pre는 null
        DISCONNECTED,
        REMOVED, // now는 null
        MODE_CHANGED // 이전에도 인터페이스가 있었을때 발생
    }
}
