package com.whoz_in.network_api.common.network_interface;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NetworkInterfaceRemoved {
        private final String interfaceName;
}
