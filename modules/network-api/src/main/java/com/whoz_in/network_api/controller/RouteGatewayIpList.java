package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


// route -n으로 구현합니다. (nux 기반 운영체제의 기본 명령인듯)
@Slf4j
@Profile("prod")
@Component
public final class RouteGatewayIpList implements GatewayIpList{
    private final List<String> list;

    public RouteGatewayIpList() {
        TransientProcess routeProcess = TransientProcess.start("route -n");
        Pattern pattern = Pattern.compile("^0\\.0\\.0\\.0\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)"); // IP 찾는 정규식
        this.list = routeProcess.results().stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(m->m.group(1))
                .toList();
        log.info("주변 게이트웨이 : {}", this.list);
    }

    @Override
    public boolean isGatewayIp(String ip) {
        return list.contains(ip);
    }
}
