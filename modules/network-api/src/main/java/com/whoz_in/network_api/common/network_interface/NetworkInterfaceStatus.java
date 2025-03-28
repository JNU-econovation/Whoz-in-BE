package com.whoz_in.network_api.common.network_interface;

public enum NetworkInterfaceStatus {
    ADDED,
    RECONNECTED,
    ADDED_AND_RECONNECTED,
    DISCONNECTED,
    REMOVED,
    MODE_CHANGED // managed가 monitor로 바뀔 일은 없으니 monitor에서만 쓰게 됨
}
