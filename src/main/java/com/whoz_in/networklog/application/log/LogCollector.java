package com.whoz_in.networklog.application.log;

// 네트워크 주소를 가져오는 프로세스
public interface LogCollector {

    void execute(NetworkLogManager manager);

}