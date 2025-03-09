package com.whoz_in.network_api.common.network_interface;

public record NetworkInterfaceStatusEvent(
        String interfaceName,
        NetworkInterface pre, // 이벤트 발생 이전 (사라졌다 생긴 경우 null)
        NetworkInterface now, // 이벤트 발생 이후 (제거된 경우 null)
        Status status
) {
    public enum Status {
        ADDED,
        RECONNECTED,
        ADDED_AND_RECONNECTED,
        DISCONNECTED,
        REMOVED,
        MODE_CHANGED
    }
}
