package com.whoz_in.network_api.common.network_interface.mac;

import com.whoz_in.network_api.common.network_interface.WirelessInfo;
import com.whoz_in.network_api.common.network_interface.WirelessInfoResolver;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class StubWirelessInfoResolver implements WirelessInfoResolver {
    @Override
    public Map<String, WirelessInfo> resolve() {
        return Map.of(
                "en0", new WirelessInfo("managed", "my_ssid")
        );
    }
}
