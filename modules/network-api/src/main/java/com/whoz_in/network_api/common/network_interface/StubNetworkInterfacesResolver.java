package com.whoz_in.network_api.common.network_interface;

import com.whoz_in.network_api.config.NetworkConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/*
기본적으로 맥에서 iwconfig를 못쓴다.
그래서 local에선 설정한 네트워크 인터페이스를 출력하는 이 가짜 객체를 만든 것이다.
 */
@Profile("local")
@Component
@RequiredArgsConstructor
public final class StubNetworkInterfacesResolver implements NetworkInterfacesResolver {
    private final NetworkConfig networkConfig;

    @Override
    public List<NetworkInterface> getLatest() {
        return networkConfig.getAllNIs();
    }
}
