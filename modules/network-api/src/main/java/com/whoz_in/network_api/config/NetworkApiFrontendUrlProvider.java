package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 기기 등록 페이지 접근 주소를 제공함
@Slf4j
@Component
public final class NetworkApiFrontendUrlProvider {
    private final NetworkInterfaceManager manager;
    private final String internalAccessInterface;
    private final String networkApiFrontendPort;

    public NetworkApiFrontendUrlProvider(
            NetworkInterfaceManager manager,
            NetworkInterfaceProfileConfig profileConfig,
            @Value("${frontend.network-api.internal-access-ssid}") String internalAccessSsid,
            @Value("${frontend.network-api.port}") String networkApiFrontendPort
    ) {
        this.manager = manager;
        // 사용자가 어떤 와이파이에 연결돼있든 network-api에 배포된 프론트로 접근할 수 있는 ssid를 인터페이스로 변환
        this.internalAccessInterface = profileConfig.getBySsid(internalAccessSsid).interfaceName();
        this.networkApiFrontendPort = networkApiFrontendPort;
    }

    public Optional<String> get(){
        NetworkInterface internalAccessNI = manager.getByName(internalAccessInterface);
        if (!internalAccessNI.isConnected()) {
            return Optional.empty();
        }
        return Optional.of("http://%s:%s".formatted(
                internalAccessNI.getNetworkAddress().ip(),
                networkApiFrontendPort
        ));
    }
}
