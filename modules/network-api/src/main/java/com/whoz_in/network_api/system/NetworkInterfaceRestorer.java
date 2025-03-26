package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus;
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
        NetworkInterfaceStatus status = event.status();

        if (status == NetworkInterfaceStatus.DISCONNECTED || status == NetworkInterfaceStatus.REMOVED) {
            reconnector.scheduleReconnection(interfaceName);
        } else if (status == NetworkInterfaceStatus.RECONNECTED || status == NetworkInterfaceStatus.ADDED) {
            reconnector.cancelReconnection(interfaceName);
        }
    }
}
