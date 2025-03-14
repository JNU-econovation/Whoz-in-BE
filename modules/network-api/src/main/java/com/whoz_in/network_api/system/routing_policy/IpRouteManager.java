package com.whoz_in.network_api.system.routing_policy;

import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class IpRouteManager {

    // 라우팅 테이블들에 대해 'ip route show table <table>' 명령어의 출력을 파싱하여 라우트 정보를 반환
    // Map<gateway, interfaceName>
    public Map<String, String> getRoutes(Set<String> rtTables) {
        return rtTables.stream()
                .map("ip route show table %s"::formatted)
                .map(TransientProcess::create)
                .map(TransientProcess::results)
                .flatMap(Collection::stream)
                .filter(line-> line.startsWith("default"))
                .map(line-> line.split("\\s+"))
                .filter(tokens-> tokens.length == 5)
                // [0] = "default", [1] = "via", [2] = gateway,
                // [3] = "dev", [4] = interface name
                .collect(Collectors.toMap(
                        tokens-> tokens[2],
                        tokens-> tokens[4]
                ));
    }

    // gateway, interface, table를 통해 라우트 추가
    public void addRoute(String gateway, String interfaceName, String table) {
        String cmd = String.format("sudo ip route add default via %s dev %s table %s", gateway, interfaceName, table);
        TransientProcess.create(cmd).waitTermination();
        log.info("[ip route] {}(interface:{}, gateway:{}) 추가됨", table, interfaceName, gateway);
    }

    // table의 default 라우트 삭제
    public void deleteByTable(String table) {
        String cmd = String.format("sudo ip route flush table %s", table);
        TransientProcess.create(cmd).waitTermination();
        log.info("[ip route] {} 삭제됨", table);
    }
}

