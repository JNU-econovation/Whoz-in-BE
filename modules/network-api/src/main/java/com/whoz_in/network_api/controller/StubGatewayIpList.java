package com.whoz_in.network_api.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


// 로컬에선 딱히 필요 없어서 구현 안했습니다.
@Profile("local")
@Component
public final class StubGatewayIpList implements GatewayIpList{
    @Override
    public boolean isGatewayIp(String ip) {
        return false;
    }
}
