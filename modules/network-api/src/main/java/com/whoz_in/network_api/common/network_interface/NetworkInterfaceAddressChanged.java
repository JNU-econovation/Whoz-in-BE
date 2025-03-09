package com.whoz_in.network_api.common.network_interface;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// now가 네트워크에 연결되어있는 경우에만 발생한다. (monitor 모드는 발생하지 않는다는 뜻이기도 하다.)
@Getter
@RequiredArgsConstructor
public class NetworkInterfaceAddressChanged {
        private final String interfaceName;
        private final NetworkInterface pre;
        private final NetworkInterface now;

        public NetworkInterfaceAddressChanged(NetworkInterface pre, NetworkInterface now) {
                this.interfaceName = now.getName();
                this.pre = pre;
                this.now = now;
        }
}
