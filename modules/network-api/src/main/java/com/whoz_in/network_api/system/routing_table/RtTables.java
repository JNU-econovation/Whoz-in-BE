package com.whoz_in.network_api.system.routing_table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public final class RtTables {
    private final Set<String> rtTables;

    public RtTables() throws IOException {
        this.rtTables = loadRtTables();
    }

    public Set<String> get(){
        return this.rtTables;
    }

    private Set<String> loadRtTables() throws IOException {
        return Files.lines(Path.of("/etc/iproute2/rt_tables"))
                .map(String::trim)                          // 공백 제거
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))  // 빈 줄 및 주석 제거
                .map(line -> line.split("\\s+"))            // 공백 기준 분할
                .filter(parts -> parts.length >= 2)         // 번호와 테이블 이름이 있는 경우만 처리
                .map(parts -> parts[1])                     // 테이블 이름만 추출
                .collect(Collectors.toUnmodifiableSet());   // 불변 Set으로 변환
    }
}
