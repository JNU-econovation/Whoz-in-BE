package com.whoz_in.network_log.infra.managed.manager;

import com.whoz_in.network_log.infra.managed.LogDTO;
import com.whoz_in.network_log.infra.managed.ManagedConfig;
import com.whoz_in.network_log.infra.managed.collector.LogProcess;
import com.whoz_in.network_log.infra.managed.parser.LogParser;
import com.whoz_in.network_log.persistence.ManagedLogDAO;
import com.whoz_in.network_log.persistence.ManagedLog;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MulticastDNSLogManager implements LogManager {

    // TODO: tShark health 체크해야 함
    // TODO: collector로부터 받은 데이터 가공해야 함
    // TODO: 올바른 wifi에 연결 됐는지 확인 해야 함
    // TODO: 모니터 모드인지 확인 해야 함

    private final ManagedLogDAO managedLogDAO;
    private final LogParser logParser;
    private final Set<LogDTO> logs = Collections.newSetFromMap(new ConcurrentHashMap<>()); // 동시성에 최적화된 Set
    private final ManagedConfig config;

    public MulticastDNSLogManager(ManagedLogDAO managedLogDAO,
                                  LogParser logParser,
                                  ManagedConfig config) {
        this.managedLogDAO = managedLogDAO;
        this.logParser = logParser;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        Arrays.stream(config.mDnsCommands())
                .map(command -> command.split(" "))
                .map(command -> new Thread(() -> {
                    LogProcess logProcess = new LogProcess(command,this);
                    logProcess.start(config.getPassword());
                }))
                .forEach(Thread::start);
    }

    @Override
    public void receive(Set<String> logs){
        // collector 로부터 받은 로그를 정제하여 DB에 저장하는 과정

        Set<LogDTO> parsed = logs.stream()
                .filter(Objects::nonNull)
                .map(logParser::parse)
                .collect(Collectors.toSet());

        this.logs.addAll(parsed);
    }

    @Override
    public void receive(String log) {
        LogDTO parsed = logParser.parse(log);

        if(parsed!=null) this.logs.add(parsed);
    }


    @Scheduled(fixedRate = 10000)
    private void saveLogs() {
        System.out.println("[managed] 저장할 로그 개수 : " + this.logs.size());
        Set<ManagedLog> entities = this.logs.stream().map(ManagedLog::create).collect(Collectors.toSet());

        managedLogDAO.bulkInsert(entities);
        this.logs.clear();
    }

}
