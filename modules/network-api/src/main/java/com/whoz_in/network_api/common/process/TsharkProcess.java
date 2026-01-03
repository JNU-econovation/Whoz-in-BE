package com.whoz_in.network_api.common.process;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.WirelessMode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TsharkProcess extends ResilientContinuousProcess {
    private final String interfaceName;
    private final NetworkInterfaceManager networkInterfaceManager;
    private boolean canStart = false;

    private TsharkProcess(String command, String interfaceName, NetworkInterfaceManager networkInterfaceManager) {
        super(command);
        this.interfaceName = interfaceName;
        this.networkInterfaceManager = networkInterfaceManager;
    }

    public static TsharkProcess create(String command, String interfaceName, NetworkInterfaceManager networkInterfaceManager) {
        TsharkProcess process = new TsharkProcess(command, interfaceName, networkInterfaceManager);
        try {
            process.start();
        } catch (Exception e) {
            log.warn("[TsharkProcess] 초기 시작 실패: {}", e.getMessage());
            // 시작 실패해도 객체는 반환 (나중에 재시도 가능)
        }
        return process;
    }

    @Override
    protected void init() throws IOException {
        // 모니터 모드 확인
        if (!isMonitorMode()) {
            log.warn("[TsharkProcess] {}가 모니터 모드가 아닙니다. tshark 실행 중단", interfaceName);
            canStart = false;
            throw new IllegalStateException("Interface " + interfaceName + " is not in monitor mode");
        }

        canStart = true;
        log.info("[TsharkProcess] 모니터 모드 확인 완료. tshark 시작");
        super.init();
    }

    @Override
    public boolean isAlive() {
        return canStart && super.isAlive();
    }

    private boolean isMonitorMode() {
        try {
            NetworkInterface networkInterface = networkInterfaceManager.getByName(interfaceName);
            return networkInterface != null &&
                    networkInterface.getWirelessInfo() != null &&
                    networkInterface.getWirelessInfo().mode() == WirelessMode.MONITOR;
        } catch (Exception e) {
            log.error("[TsharkProcess] 모니터 모드 확인 실패: {}", e.getMessage());
            return false;
        }
    }
}
