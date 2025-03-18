package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkInterfaceRestorer {

    private final UsbReconnector reconnector;

    @EventListener
    public void handle(NetworkInterfaceStatusEvent event) {
        String interfaceName = event.interfaceName();
        Status status = event.status();

        if (status == Status.DISCONNECTED || status == Status.REMOVED) {
            reconnector.scheduleReconnection(interfaceName);
            log.info("{}의 USB 초기화가 예약됐습니다. (10초 후 실행)", interfaceName);

        } else if (status == Status.RECONNECTED || status == Status.ADDED) {
            reconnector.cancelReconnection(interfaceName);
            log.info("{}의 USB 초기화 예약이 취소됐습니다. (다시 연결됨)", interfaceName);
        }
    }
}

