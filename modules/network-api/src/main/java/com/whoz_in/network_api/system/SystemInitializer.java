package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.system.routing_table.RtTables;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
@DependsOn("systemStartupValidator") // 시스템의 초기 상태가 검증된 이후에 초기화 작업 시작하기 위함
public final class SystemInitializer {
    public SystemInitializer(
            RtTables rtTables,
            IpRuleManager ipRuleManager,
            IpRouteManager ipRouteManager,
            NetworkInterfaceProfileConfig profileConfig,
            NetworkInterfaceManager niManager
        ) {
        Map<String, NetworkInterface> niMap = niManager.get();
        List<NetworkInterface> wirelessManagedNIs = profileConfig.getManagedProfiles()
                .stream()
                .map(NetworkInterfaceProfile::interfaceName)
                .map(niMap::get)
                .filter(NetworkInterface::isWireless)
                .toList();

        // 기존에 등록된 모든 ip rule 삭제
        ipRuleManager.deleteAllRules();
        // rt_tables에 등록된 모든 테이블의 ip route 삭제
        rtTables.get().forEach(ipRouteManager::removeRoutesByTable);

        // 현재 기준으로 ip rule과 route 등록
        List<String> rtTableList = rtTables.get().stream().toList();
        int index = 0;
        for (NetworkInterface ni : wirelessManagedNIs){
            ipRuleManager.addRule(ni.getNetworkAddress().ip(), rtTableList.get(index));
            ipRouteManager.addRoute(ni.getNetworkAddress().gateway(), ni.getName(), rtTableList.get(index));
            index++;
        }
    }
}
