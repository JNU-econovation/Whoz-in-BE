package com.whoz_in.networklog.application.log.collector;

import com.whoz_in.networklog.application.log.manager.MulticastDNSLogManager;

// 네트워크 주소를 가져오는 프로세스
public interface LogCollector {

    void collect();

}