package com.whoz_in.network_api.common.network_interface;

public record NetworkInterfaceStatusEvent(
        String interfaceName,
        NetworkInterface pre,
        NetworkInterface now,
        NetworkInterfaceStatus status
) {

}
