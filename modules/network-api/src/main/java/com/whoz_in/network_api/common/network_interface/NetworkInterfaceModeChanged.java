package com.whoz_in.network_api.common.network_interface;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NetworkInterfaceModeChanged {
        private final String interfaceName;
        private final NetworkAddress pre;
        private final NetworkAddress now;

        public NetworkInterfaceModeChanged(NetworkInterface pre, NetworkInterface now) {
                this.interfaceName = pre.getName();
                this.pre = pre.getNetworkAddress();
                this.now = now.getNetworkAddress();
        }
}
