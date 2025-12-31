package com.whoz_in.network_api.common.network_interface;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemInterfaceModeDetector implements InterfaceModeChecker{
    private final NetworkInterfaceManager manager;

    @Override
    public boolean isMonitorMode(String interfaceName) {
        try {
            Map<String, NetworkInterface> interfaces = manager.get();
            NetworkInterface ni = interfaces.get(interfaceName);

            if (ni == null) {
                log.warn("[ModeDetector] {} 인터페이스를 찾을 수 없음", interfaceName);
                return false;
            }

            boolean isMonitor = ni.getWirelessInfo().mode() == WirelessMode.MONITOR;
            log.debug("[ModeDetector] {} 모드: {}", interfaceName,
                    ni.getWirelessInfo().mode());

            return isMonitor;

        } catch (Exception e) {
            log.error("[ModeDetector] {} 모드 확인 실패", interfaceName, e);
            return false;
        }
    }
}
