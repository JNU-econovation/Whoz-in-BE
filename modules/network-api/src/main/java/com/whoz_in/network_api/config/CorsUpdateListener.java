package com.whoz_in.network_api.config;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.ADDED_AND_RECONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.RECONNECTED;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 변경된 내부 아이피가 대표 내부 아이피인 경우(network api에서 실행되는 프론트로 접근하는 아이피) CORS에 추가함
@Slf4j
@Profile("prod")
@Component
public class CorsUpdateListener {
    private final DynamicCorsConfigurationSource dynamicCorsConfigurationSource;
    private final String internalAccessInterface;
    private final NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider;

    public CorsUpdateListener(
            NetworkInterfaceProfileConfig profileConfig,
            DynamicCorsConfigurationSource dynamicCorsConfigurationSource,
            @Value("${frontend.network-api.internal-access-ssid}") String internalAccessSsid,
            NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider
    ) {
        this.networkApiFrontendUrlProvider = networkApiFrontendUrlProvider;
        this.dynamicCorsConfigurationSource = dynamicCorsConfigurationSource;
        this.internalAccessInterface = profileConfig.getBySsid(internalAccessSsid).interfaceName();
    }

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        // 재연결일 때만 처리
        if (event.status() != RECONNECTED && event.status() != ADDED_AND_RECONNECTED) return;
        // 해당 인터페이스에 대한 이벤트일때만 처리
        if (!event.now().getName().equals(internalAccessInterface)) return;

        dynamicCorsConfigurationSource.addAllowedOrigin(networkApiFrontendUrlProvider.get().get()); // 이상적으론 무조건 값이 존재
    }
}
