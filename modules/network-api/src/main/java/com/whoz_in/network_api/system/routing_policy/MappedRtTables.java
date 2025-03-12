package com.whoz_in.network_api.system.routing_policy;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public final class MappedRtTables {
    private final Map<String, String> rtTables; // Map<ni name, table>

    public String getByInterfaceName(String interfaceName){
        String table = rtTables.get(interfaceName);
        if (table == null) throw new IllegalStateException(interfaceName + "는 managed인 무선 인터페이스가 아님");
        return table;
    }

    public Map<String, String> get(){
        return this.rtTables;
    }

    public MappedRtTables(
            NetworkInterfaceProfileConfig profileConfig,
            NetworkInterfaceManager niManager
    ) throws IOException {

        Map<String, NetworkInterface> niMap = niManager.get();

        List<String> wirelessManagedNIs = profileConfig.getManagedProfiles()
                .stream()
                .map(NetworkInterfaceProfile::interfaceName)
                .map(niMap::get)
                .filter(NetworkInterface::isWireless)
                .map(NetworkInterface::getName)
                .toList();

        List<String> tables = Files.lines(Path.of("/etc/iproute2/rt_tables"))
                .map(String::trim)                          // 공백 제거
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))  // 빈 줄 및 주석 제거
                .map(line -> line.split("\\s+"))            // 공백 기준 분할
                .filter(parts -> parts.length >= 2)         // 번호와 테이블 이름이 있는 경우만 처리
                .map(parts -> parts[1])                     // 테이블 이름만 추출
                .toList();

        if (wirelessManagedNIs.size() != tables.size())
            throw new IllegalStateException("wireless인 managed 인터페이스 개수와(%d) rt_tables의 테이블 개수가(%d) 맞지 않습니다.".formatted(wirelessManagedNIs.size(), tables.size()));

        this.rtTables = IntStream.range(0, tables.size())
                .boxed()
                .collect(Collectors.toMap(wirelessManagedNIs::get, tables::get));
    }
}
