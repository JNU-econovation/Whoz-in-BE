package com.whoz_in.network_api.common.network_interface;

public enum WirelessMode {
    MANAGED,
    MONITOR;

    public static WirelessMode fromString(String mode) {
        for (WirelessMode wm : WirelessMode.values()) {
            if (wm.name().equalsIgnoreCase(mode)) {
                return wm;
            }
        }
        throw new IllegalArgumentException("Unknown wireless mode: " + mode);
    }
}
