package com.whoz_in.network_log.infra.monitor;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public final class MonitorLogParser {
    /**
     * @param log
     * 한 줄 데이터만 들어와야 한다. 들어온 데이터중 \t가 1개가 존재하는 것만 처리한다.
     * @return log에 존재하는 mac의 개수이다.
     */
    public Set<String> parse(String log) {
        String[] logParts = log.split("\t");
        Set<String> macs = new HashSet<>();

        if(logParts.length != 2) return macs; //정상적인 로그가 아닌경우
        macs.add(logParts[0]); //출발지 맥
        macs.add(logParts[1]); //도착지 맥

        //빈 문자열이 아닌 경우가 훨씬 많으므로 isEmpty()를 사용하지 않고, 저장하기 전에 빈 문자열을 없애도록 만들었음

        return macs;
    }
}