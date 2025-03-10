package com.whoz_in.network_api.common.network_interface;

public record NetworkInterfaceStatusEvent(
        String interfaceName,
        NetworkInterface pre,
        NetworkInterface now,
        Status status
) {
    public enum Status {
        ADDED, // pre null
        RECONNECTED,
        ADDED_AND_RECONNECTED, // pre null
        DISCONNECTED,
        REMOVED, // now null
        MODE_CHANGED
    }
}
