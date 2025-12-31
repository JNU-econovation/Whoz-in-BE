package com.whoz_in.network_api.common.network_interface;

@FunctionalInterface
public interface InterfaceModeChecker {
    boolean isMonitorMode(String interfaceName);
}
