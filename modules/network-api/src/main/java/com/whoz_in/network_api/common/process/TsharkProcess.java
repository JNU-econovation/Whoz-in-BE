package com.whoz_in.network_api.common.process;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.WirelessMode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TsharkProcess extends ResilientContinuousProcess {
    private final String interfaceName;
    private final NetworkInterfaceManager networkInterfaceManager;

    private TsharkProcess(String command, String interfaceName, NetworkInterfaceManager networkInterfaceManager) {
        super(command);
        this.interfaceName = interfaceName;
        this.networkInterfaceManager = networkInterfaceManager;
    }

    public static TsharkProcess create(String command, String interfaceName, NetworkInterfaceManager networkInterfaceManager) {
        TsharkProcess tp = new TsharkProcess(command, interfaceName, networkInterfaceManager);
        tp.start();
        return tp;
    }

    @Override
    protected boolean shouldStartProcess() {
        return isMonitorMode();
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
