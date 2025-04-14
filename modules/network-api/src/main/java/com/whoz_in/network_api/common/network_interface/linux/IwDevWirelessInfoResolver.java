package com.whoz_in.network_api.common.network_interface.linux;

import com.whoz_in.network_api.common.network_interface.WirelessInfo;
import com.whoz_in.network_api.common.network_interface.WirelessInfoResolver;
import com.whoz_in.network_api.common.network_interface.WirelessMode;
import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Conditional(LinuxCondition.class)
@Component
public final class IwDevWirelessInfoResolver implements WirelessInfoResolver {

    @Override
    public Map<String, WirelessInfo> resolve() {
        return parse(TransientProcess.create("iw dev").results());
    }

    private static Map<String, WirelessInfo> parse(List<String> results) {
        Map<String, WirelessInfo> wirelessMap = new HashMap<>();
        String currentInterface = null;
        WirelessMode mode = null;
        String ssid = null;

        for (String line : results) {
            line = line.trim();
            if (line.startsWith("Interface")) {
                if (currentInterface != null) {
                    wirelessMap.put(currentInterface, new WirelessInfo(mode, ssid));
                }
                currentInterface = line.replace("Interface", "").trim();
                mode = null;
                ssid = null;
            }
            if (line.contains("type") && currentInterface != null) {
                mode = WirelessMode.fromString(line.split("type")[1].trim());
            }
            if (line.contains("ssid") && currentInterface != null) {
                ssid = line.split("ssid")[1].trim();
            }
        }
        if (currentInterface != null) {
            wirelessMap.put(currentInterface, new WirelessInfo(mode, ssid));
        }
        return wirelessMap;
    }
}
