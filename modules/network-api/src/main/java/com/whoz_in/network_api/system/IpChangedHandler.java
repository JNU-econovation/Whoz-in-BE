package com.whoz_in.network_api.system;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.*;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.config.DynamicCorsConfigurationSource;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class IpChangedHandler {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final DynamicCorsConfigurationSource dynamicCorsConfigurationSource;

    // cors 처리 메서드
    private void changeCorsOrigin(NetworkInterfaceStatusEvent event){
        dynamicCorsConfigurationSource.addAllowedOrigin(event.now().getNetworkAddress().ip());
    }
    // 정책 기반 라우팅 처리 메서드


    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        if (event.status() != RECONNECTED && event.status() != ADDED_AND_RECONNECTED) return;
        changeCorsOrigin(event);
        NetworkInterface now = event.now();
        if (!now.isWireless()) return; // 유선일 경우 종료

    }
}
