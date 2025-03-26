package com.whoz_in.network_api.system;

import static com.whoz_in.network_api.common.network_interface.WirelessMode.MONITOR;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.WirelessMode;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.system.routing_policy.PolicyRoutingInitializer;
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
            PolicyRoutingInitializer policyRoutingInitializer,
            NetworkInterfaceManager niManager,
            MonitorModeSwitcher monitorModeSwitcher,
            NetworkInterfaceProfileConfig profileConfig
        ) {
        policyRoutingInitializer.initialize();

        NetworkInterface monitorInterface = niManager.get().get(profileConfig.getMonitorProfile().interfaceName());
        WirelessMode mode = monitorInterface.getWirelessInfo().mode();
        if (mode != MONITOR) {
            log.info("{}가 모니터 모드가 아닙니다. (현재 {})", monitorInterface.getName(), mode);
            // 모니터 모드로 전환
            monitorModeSwitcher.switchToMonitor();
        }
    }
}
