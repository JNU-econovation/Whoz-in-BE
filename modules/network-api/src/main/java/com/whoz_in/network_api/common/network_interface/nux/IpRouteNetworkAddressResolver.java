package com.whoz_in.network_api.common.network_interface.nux;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkAddressResolver;
import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// TODO: 필요한 명령어를 어노테이션으로 작성? sh을 실행할수도 있는데 어떻게 하지
@Profile("prod")
@Component
public final class IpRouteNetworkAddressResolver implements NetworkAddressResolver {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, NetworkAddress> resolve() {
        return parse(TransientProcess.create("ip -j route show default").result());
    }

    private static Map<String, NetworkAddress> parse(String result) {
        Map<String, NetworkAddress> connectionInfoMap = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(result);
            for (JsonNode route : root) {
                if (route.has("dev") && route.has("gateway") && route.has("prefsrc")) {
                    String iface = route.get("dev").asText();
                    String ip = route.get("prefsrc").asText();
                    String gateway = route.get("gateway").asText();
                    connectionInfoMap.put(iface, new NetworkAddress(ip, gateway));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse connection info: " + e.getMessage());
        }
        return connectionInfoMap;
    }

}
