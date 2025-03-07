package com.whoz_in.network_api.common.network_interface.mac;

import com.whoz_in.network_api.common.network_interface.ConnectionInfo;
import com.whoz_in.network_api.common.network_interface.ConnectionInfoResolver;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class StubConnectionInfoResolver implements ConnectionInfoResolver {
    @Override
    public Map<String, ConnectionInfo> resolve() {
        return Map.of("en0", new ConnectionInfo("192.168.10.15", "192.168.10.1"));
    }
}
