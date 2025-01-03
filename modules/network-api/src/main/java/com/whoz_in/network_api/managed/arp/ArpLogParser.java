package com.whoz_in.network_api.managed.arp;

import com.whoz_in.network_api.managed.ParsedLog;
import org.springframework.stereotype.Component;

@Component
public final class ArpLogParser {
    private static final String MAC_REGEX = "^([0-9A-Fa-f]{2}([-:])(?=[0-9A-Fa-f]{2}([-:]))[0-9A-Fa-f]{2}([-:])){5}[0-9A-Fa-f]{2}$|^[0-9A-Fa-f]{4}(\\.[0-9A-Fa-f]{4}){2}$";
    private static final String IP_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";


    // 파라미터로 전달된 한 줄의 로그가 유효한지 검사
    public boolean validate(String log){
        if(log.split("\t").length!=3) return false;
        if(log.split("\t")[0].matches(MAC_REGEX)) return false;
        return !log.split("\t")[1].matches(IP_REGEX);
    }

    public ParsedLog parse(String log){
        String[] splited = log.split("\t");
        //0:ip 1:mac 2:devicename(없을 수도 있어서 인자를 null로 넣음)
        return new ParsedLog(splited[1], splited[0], null);
    }
}
