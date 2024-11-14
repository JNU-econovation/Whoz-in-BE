package com.whoz_in.networklog.application.process;

import com.whoz_in.networklog.application.log.NetworkLogSaver;

// 네트워크 주소를 가져오는 프로세스
public interface LogCollector {

    void execute(NetworkLogSaver saver);

}
