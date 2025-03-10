package com.whoz_in.network_api.config;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.ADDED_AND_RECONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.RECONNECTED;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
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
    private final NetworkInterfaceProfileConfig profileConfig;
    private final DynamicCorsConfigurationSource dynamicCorsConfigurationSource;
    private final String internalAccessSsid;
    private final String networkApiFrontendPort;

    public CorsUpdateListener(
            NetworkInterfaceProfileConfig profileConfig,
            DynamicCorsConfigurationSource dynamicCorsConfigurationSource,
            @Value("${frontend.network-api.internal-access-ssid}") String internalAccessSsid,
            @Value("${frontend.network-api.port}") String networkApiFrontendPort
    ) {
        this.profileConfig = profileConfig;
        this.dynamicCorsConfigurationSource = dynamicCorsConfigurationSource;
        this.internalAccessSsid = internalAccessSsid;
        this.networkApiFrontendPort = networkApiFrontendPort;
    }

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        // 재연결일 때만 처리
        if (event.status() != RECONNECTED && event.status() != ADDED_AND_RECONNECTED) return;

        // 사용자가 어떤 와이파이에 연결돼있든 network-api에 배포된 프론트로 접근할 수 있는 ssid를 인터페이스로 변환
        String internalAccessInterface = profileConfig.getBySsid(internalAccessSsid).interfaceName();

        // 해당 인터페이스에 대한 이벤트일때만 처리
        if (!event.now().getName().equals(internalAccessInterface)) return;

        // CORS origin에 추가할 origin 생성
        String origin = "http://"+ event.now().getNetworkAddress().ip() + ":" + networkApiFrontendPort;
        dynamicCorsConfigurationSource.addAllowedOrigin(origin);
    }
}
