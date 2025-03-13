package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 기기 등록 페이지 접근 주소를 제공함
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

    public String get(){
        NetworkInterface internalAccessNI = manager.getByName(internalAccessInterface);
        if (!internalAccessNI.isConnected()) throw new IllegalStateException(internalAccessNI + "가 인터넷에 연결되어있지 않음");
        return "http://%s:%s".formatted(
                internalAccessNI.getNetworkAddress().ip(),
                networkApiFrontendPort
        );
    }
}
