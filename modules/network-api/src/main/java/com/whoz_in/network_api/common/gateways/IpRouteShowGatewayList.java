package com.whoz_in.network_api.common.gateways;

import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// `ip route show` 명령어를 사용하여 기본 게이트웨이를 찾는 클래스
@Slf4j
@Profile("prod")
@Component
public final class IpRouteShowGatewayList implements GatewayList {
    private final List<String> list;

    public IpRouteShowGatewayList() {
        TransientProcess routeProcess = TransientProcess.start("ip route show");
        // 기본 게이트웨이 찾는 정규식 (예: "default via 192.168.0.1 dev wlan0 proto dhcp src 192.168.0.101")
        Pattern pattern = Pattern.compile("^default via (\\d+\\.\\d+\\.\\d+\\.\\d+)");

        this.list = routeProcess.results().stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(m -> m.group(1))
                .toList();

        log.info("주변 게이트웨이 : {}", this.list);
    }

    @Override
    public boolean isGatewayIp(String ip) {
        return list.contains(ip);
    }
}
