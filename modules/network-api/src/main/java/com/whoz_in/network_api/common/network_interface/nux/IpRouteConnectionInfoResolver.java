package com.whoz_in.network_api.common.network_interface.nux;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoz_in.network_api.common.network_interface.ConnectionInfo;
import com.whoz_in.network_api.common.network_interface.ConnectionInfoResolver;
import com.whoz_in.network_api.common.process.TransientProcess;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// TODO: 필요한 명령어를 어노테이션으로 작성
@Profile("prod")
@Component
public final class IpRouteConnectionInfoResolver implements ConnectionInfoResolver {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, ConnectionInfo> resolve() {
        return parse(TransientProcess.create("ip -j route show default").result());
    }

    private static Map<String, ConnectionInfo> parse(String result) {
        Map<String, ConnectionInfo> connectionInfoMap = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(result);
            for (JsonNode route : root) {
                if (route.has("dev") && route.has("gateway") && route.has("src")) {
                    String iface = route.get("dev").asText();
                    String ip = route.get("src").asText();
                    String gateway = route.get("gateway").asText();
                    connectionInfoMap.put(iface, new ConnectionInfo(ip, gateway));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse connection info: " + e.getMessage());
        }
        return connectionInfoMap;
    }

}
