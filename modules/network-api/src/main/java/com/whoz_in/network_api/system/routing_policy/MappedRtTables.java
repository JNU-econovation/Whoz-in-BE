package com.whoz_in.network_api.system.routing_policy;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.LinuxCondition;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(LinuxCondition.class)
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

    public Collection<String> getTables(){
        return this.rtTables.values();
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
                .map(String::trim) // 화이트 스페이스 삭제
                .filter(table-> !List.of("main", "default", "local", "unspec").contains(table)) // 기본 테이블 제외
                .toList();

        if (tables.size() != new HashSet<>(tables).size())
            throw new IllegalStateException("중복 정의된 rt_table이 있습니다.");
        if (wirelessManagedNIs.size() != tables.size())
            throw new IllegalStateException("wireless인 managed 인터페이스 개수와(%d) rt_tables의 테이블 개수가(%d) 맞지 않습니다.".formatted(wirelessManagedNIs.size(), tables.size()));

        this.rtTables = IntStream.range(0, tables.size())
                .boxed()
                .collect(Collectors.toMap(wirelessManagedNIs::get, tables::get));
    }
}
