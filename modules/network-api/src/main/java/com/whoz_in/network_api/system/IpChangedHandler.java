package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceAddressChanged;
import com.whoz_in.network_api.config.DynamicCorsConfigurationSource;
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
    private final DynamicCorsConfigurationSource dynamicCorsConfigurationSource;

    // cors 처리 메서드
    private void changeCorsOrigin(NetworkInterfaceAddressChanged event){
        dynamicCorsConfigurationSource.removeAllowedOrigin(event.getPre().getNetworkAddress().ip());
        dynamicCorsConfigurationSource.addAllowedOrigin(event.getNow().getNetworkAddress().ip());
    }
    // 정책 기반 라우팅 처리 메서드


    @EventListener
    private void handleIpChanged(NetworkInterfaceAddressChanged event) {
        NetworkInterface now = event.getNow();
        changeCorsOrigin(event);
        if (!now.isWireless()) return; // 유선일 경우 종료

    }
}
