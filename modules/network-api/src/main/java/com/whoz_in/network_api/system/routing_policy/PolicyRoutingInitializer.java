package com.whoz_in.network_api.system.routing_policy;

import static com.whoz_in.network_api.common.network_interface.WirelessMode.MANAGED;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
@RequiredArgsConstructor
public class PolicyRoutingInitializer {
    private final NetworkInterfaceManager networkInterfaceManager;
    private final IpRouteManager ipRouteManager;
    private final IpRuleManager ipRuleManager;
    private final MappedRtTables mappedRtTables;


    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        // 다시 연결됐을때
        if (event.status() == NetworkInterfaceStatus.RECONNECTED || event.status() == NetworkInterfaceStatus.ADDED_AND_RECONNECTED) {
            if (!networkInterfaceManager.available(MANAGED)) return;
            this.initialize();
        }
    }


    public void initialize(){
        Map<String, NetworkInterface> niMap = networkInterfaceManager.get();
        // 기존에 등록된 모든 ip rule 삭제
        ipRuleManager.deleteAllRules();
        // rt_tables에 등록된 모든 테이블의 ip route 삭제
        mappedRtTables.get().values().forEach(ipRouteManager::deleteByTable);

        // 현재 기준으로 ip rule과 route 등록
        Map<String, String> wirelessInterfaces = mappedRtTables.get(); // Map<wireless interface name, table>
        wirelessInterfaces.forEach((interfaceName, table)->{
            NetworkInterface wirelessInterface = niMap.get(interfaceName);
            ipRuleManager.addRule(wirelessInterface.getNetworkAddress().ip(), table);
            ipRouteManager.addRoute(wirelessInterface.getNetworkAddress().gateway(), interfaceName, table);
        });
    }
}
