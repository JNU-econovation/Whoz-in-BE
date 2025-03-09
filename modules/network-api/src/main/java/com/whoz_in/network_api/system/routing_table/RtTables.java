package com.whoz_in.network_api.system.routing_table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public final class RtTables {
    private final Map<Integer, String> rtTables;

    public Map<Integer, String> get(){
        return this.rtTables;
    }

    public RtTables() throws IOException {
        Map<Integer, String> rtTables = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of("/etc/iproute2/rt_tables"));

        for (String line : lines) {
            line = line.trim();
            // 빈 줄이나 주석은 건너뜀
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            // 공백으로 분리 (번호와 테이블 이름)
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                try {
                    int tableId = Integer.parseInt(parts[0]);
                    String tableName = parts[1];
                    rtTables.put(tableId, tableName);
                } catch (NumberFormatException e) {
                    log.error("형식에 맞지 않는 줄: " + line);
                }
            }
        }
        this.rtTables = Map.copyOf(rtTables);
    }
}

