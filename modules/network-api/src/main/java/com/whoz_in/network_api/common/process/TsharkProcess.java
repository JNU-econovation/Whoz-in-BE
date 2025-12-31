package com.whoz_in.network_api.common.process;

import com.whoz_in.network_api.common.network_interface.InterfaceModeChecker;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TsharkProcess extends ResilientContinuousProcess{
    private final String interfaceName;
    private final InterfaceModeChecker modeChecker;

    TsharkProcess(String command, String interfaceName, InterfaceModeChecker modeChecker) {
        super(command);
        this.interfaceName = interfaceName;
        this.modeChecker = modeChecker;
    }

    public static TsharkProcess create(String command, String interfaceName, InterfaceModeChecker modeChecker) {
        TsharkProcess tshark = new TsharkProcess(command, interfaceName, modeChecker);
        tshark.start();
        return tshark;
    }

    @Override
    protected void init() throws IOException {
        // 모니터 모드 확인
        if (!modeChecker.isMonitorMode(interfaceName)) {
            String message = String.format(
                    "[TsharkProcess] %s가 모니터 모드가 아닙니다. tshark를 시작하지 않습니다.",
                    interfaceName
            );
            log.warn(message);
            throw new IllegalStateException(message);
        }

        log.info("[TsharkProcess] {}가 모니터 모드 확인됨. tshark 시작", interfaceName);
        super.init(); // 프로세스 시작
    }

    @Override
    public synchronized void restart() {
        log.info("[TsharkProcess] 재시작 시도. 모니터 모드 재확인");
        super.restart();
    }
}
