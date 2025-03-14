package com.whoz_in.network_api.common.network_interface.mac;

import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkAddressResolver;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class StubNetworkAddressResolver implements NetworkAddressResolver {
    @Override
    public Map<String, NetworkAddress> resolve() {
        return Map.of("en0", new NetworkAddress("192.168.10.15", "192.168.10.1"));
    }
}
