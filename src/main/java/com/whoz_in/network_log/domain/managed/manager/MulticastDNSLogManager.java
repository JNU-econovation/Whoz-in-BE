package com.whoz_in.network_log.domain.managed.manager;

import com.whoz_in.network_log.domain.managed.LogDTO;
import com.whoz_in.network_log.domain.managed.collector.LogCollector;
import com.whoz_in.network_log.domain.managed.parser.LogParser;
import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final Set<LogDTO> logs = new HashSet<>();

    public MulticastDNSLogManager(LogCollector logCollector,
                                  LogRepository logRepository,
                                  LogParser logParser) {
        this.logCollector = logCollector;
        this.logRepository = logRepository;
        this.logParser = logParser;

        this.logCollector.setManager(this);
        this.logCollector.collect();
    }

    @Override
    public void receive(Set<String> logs){
        // collector 로부터 받은 로그를 정제하여 DB에 저장하는 과정

        Set<LogDTO> parsed = logs.stream()
                .map(logParser::parse)
                .collect(Collectors.toSet());

        this.logs.addAll(parsed);
    }

    @Override
    public void receive(String log) {
        LogDTO parsed = logParser.parse(log);

        this.logs.add(parsed);
    }


    @Scheduled(fixedRate = 10000)
    private void saveLogs() {
        System.out.println("[managed] 저장할 로그 개수 : " + this.logs.size());
        Set<ManagedLog> entities = this.logs.stream().map(ManagedLog::create).collect(Collectors.toSet());

        logRepository.bulkInsert(entities);
    }

}
