package com.whoz_in.network_api.common.network_interface.nonLinux;

import com.whoz_in.network_api.common.NonLinuxCondition;
import com.whoz_in.network_api.common.network_interface.WirelessInfo;
import com.whoz_in.network_api.common.network_interface.WirelessInfoResolver;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.whoz_in.network_api.common.network_interface.WirelessMode.MANAGED;

@Conditional(NonLinuxCondition.class)
@Component
public class StubWirelessInfoResolver implements WirelessInfoResolver {
    @Override
    public Map<String, WirelessInfo> resolve() {
        return Map.of(
                "en0", new WirelessInfo(MANAGED, "my_ssid")
        );
    }
}
