package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.system.routing_policy.IpRouteManager;
import com.whoz_in.network_api.system.routing_policy.IpRuleManager;
import com.whoz_in.network_api.system.routing_policy.MappedRtTables;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// managed 인터페이스 중 무선 인터페이스들을 라우팅 테이블에 매칭시켜놓음
@Slf4j
@Profile("prod")
@Component
@DependsOn("systemStartupValidator") // 시스템의 초기 상태가 검증된 이후에 초기화 작업 시작하기 위함
public final class SystemInitializer {
    public SystemInitializer(
            MappedRtTables mappedRtTables,
            IpRuleManager ipRuleManager,
            IpRouteManager ipRouteManager,
            NetworkInterfaceManager niManager,
            MonitorModeSwitcher monitorModeSwitcher,
            NetworkInterfaceProfileConfig profileConfig
        ) {

        Map<String, NetworkInterface> niMap = niManager.get();

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


        NetworkInterface monitorInterface = niMap.get(profileConfig.getMonitorProfile().interfaceName());
        String mode = monitorInterface.getWirelessInfo().mode();
        if (!mode.equals("monitor")) {
            log.info("{}가 모니터 모드가 아닙니다. (현재 {})", monitorInterface.getName(), mode);
            // 모니터 모드로 전환
            monitorModeSwitcher.switchToMonitor();
        }
    }
}
