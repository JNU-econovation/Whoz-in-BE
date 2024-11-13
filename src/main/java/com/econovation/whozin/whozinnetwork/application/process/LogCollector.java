package com.econovation.whozin.whozinnetwork.application.process;

import com.econovation.whozin.whozinnetwork.application.log.NetworkLogSaver;

// 네트워크 주소를 가져오는 프로세스
public interface LogCollector {

    void execute(NetworkLogSaver saver);

}
