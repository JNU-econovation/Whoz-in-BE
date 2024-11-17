package com.whoz_in.network_log.application.log.manager;

import com.whoz_in.network_log.application.log.collector.LogCollector;
import com.whoz_in.network_log.application.log.parser.LogParser;
import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.infrastructure.jpa.log.NetworkLog;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MulticastDNSLogManager implements LogManager {

    // TODO: tShark health 체크해야 함
    // TODO: collector로부터 받은 데이터 가공해야 함
    // TODO: 올바른 wifi에 연결 됐는지 확인 해야 함
    // TODO: 모니터 모드인지 확인 해야 함

    private final LogCollector logCollector;
    private final LogRepository logRepository;
    private final LogParser logParser;

    public MulticastDNSLogManager(LogCollector logCollector,
                                  LogRepository logRepository,
                                  LogParser logParser) {
        this.logCollector = logCollector;
        this.logRepository = logRepository;
        this.logParser = logParser;

        this.logCollector.collect();
    }

    @Override
    public void callBack(Set<String> logs){
        logs.forEach(System.out::println);
        // collector 로부터 받은 로그를 정제하여 DB에 저장하는 과정

        Set<Map<String, String>> parsed = logs.stream()
                .map(logParser::parse)
                .collect(Collectors.toSet());

        saveLogs(parsed);
    }

    private void saveLogs(Set<Map<String, String>> parsed) {
        Set<NetworkLog> entities = parsed.stream().map(NetworkLog::create).collect(Collectors.toSet());

        logRepository.saveAll(entities);
    }

}
