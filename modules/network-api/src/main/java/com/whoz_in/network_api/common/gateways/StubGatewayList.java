package com.whoz_in.network_api.common.gateways;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


// 로컬에선 딱히 필요 없어서 구현 안했습니다.
@Profile("local")
@Component
public final class StubGatewayList implements GatewayList {
    @Override
    public boolean isGatewayIp(String ip) {
        return false;
    }
}
