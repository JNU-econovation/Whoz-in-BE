package com.whoz_in.network_api.system;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.*;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus;
import com.whoz_in.network_api.common.LinuxCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(LinuxCondition.class)
@RequiredArgsConstructor
public class NetworkInterfaceRestorer {

    private final UsbReconnector reconnector;

    @EventListener
    public void handle(NetworkInterfaceStatusEvent event) {
        String interfaceName = event.interfaceName();
        NetworkInterfaceStatus status = event.status();

        if (status == DISCONNECTED || status == REMOVED) {
            reconnector.scheduleReconnection(interfaceName);
        } else if (status == RECONNECTED || status == ADDED) {
            reconnector.cancelReconnection(interfaceName);
        }
    }
}
