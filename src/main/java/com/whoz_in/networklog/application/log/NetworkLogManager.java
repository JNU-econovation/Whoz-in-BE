package com.whoz_in.networklog.application.log;

import com.whoz_in.networklog.domain.log.repository.LogRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class NetworkLogManager {

    // TODO: tShark health 체크해야 함
    // TODO: collector로부터 받은 데이터 가공해야 함
    // TODO: 올바른 wifi에 연결 됐는지 확인 해야 함
    // TODO: 모니터 모드인지 확인 해야 함

    private final LogCollector logCollector;
    private final LogRepository logRepository;

    public NetworkLogManager(LogCollector logCollector,
                             LogRepository logRepository) {
        this.logCollector = logCollector;
        this.logRepository = logRepository;
        this.logCollector.execute(this);
    }

    public void callBack(Set<String> logs){
        logs.forEach(System.out::println);
    }


}
