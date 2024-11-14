package com.whoz_in.networklog.application.log;

import com.whoz_in.networklog.application.process.LogCollector;
import com.whoz_in.networklog.domain.log.repository.LogRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class NetworkLogSaver {

    private final LogCollector logCollector;
    private final LogRepository logRepository;

    public NetworkLogSaver(LogCollector logCollector,
                           LogRepository logRepository) {
        this.logCollector = logCollector;
        this.logRepository = logRepository;
        this.logCollector.execute(this);
    }

    public void callBack(Set<String> logs){
        logs.forEach(System.out::println);
    }



}
