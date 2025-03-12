package com.whoz_in.network_api.system.routing_policy;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public final class PolicyRoutingUpdater {
    private final IpRuleManager ipRuleManager;
    private final IpRouteManager ipRouteManager;
    private final MappedRtTables mappedRtTables;

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        // 다시 연결됐을때
        if (event.status() == Status.RECONNECTED || event.status() == Status.ADDED_AND_RECONNECTED) {
            NetworkInterface now = event.now();
            if (!now.isWireless()) return; // 유선은 라우팅 테이블 관리 안하니까
            String table = mappedRtTables.getByInterfaceName(now.getName());
            log.info("{}에 다시 연결되어 {}에 관한 라우팅 정책을 수정합니다.", now.getName(), table);
            ipRuleManager.deleteByTable(table);
            ipRouteManager.deleteByTable(table);
            ipRuleManager.addRule(now.getNetworkAddress().ip(), table);
            ipRouteManager.addRoute(now.getNetworkAddress().gateway(), now.getName(), table);
        }
    }

}
