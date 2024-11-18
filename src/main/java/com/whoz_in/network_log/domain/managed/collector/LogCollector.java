package com.whoz_in.network_log.domain.managed.collector;

import com.whoz_in.network_log.domain.managed.manager.LogManager;

// 네트워크 주소를 가져오는 프로세스
public interface LogCollector {

    void collect();
    
    void setManager(LogManager manager);

}