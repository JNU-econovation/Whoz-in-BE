package com.whoz_in.network_log.domain.managed.parser;

import com.whoz_in.network_log.domain.managed.LogDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

// TODO: 테스트하기
@Component
public class DefaultLogParser implements LogParser{

    // TODO: Regex가 맞는지 검증해야 함
    private final String macRegex = "^([0-9A-Fa-f]{2}([-:])){5}[0-9A-Fa-f]{2}$";
    private final String ipRegex = "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)";
    private final String timeRegex = "[A-Z][a-z]{2} \\d{1,2}, \\d{4} \\d{2}:\\d{2}:\\d{2}\\.\\d{6,9} [A-Z]{3}";
    private final String deviceRegex = "[가-힣\\w\\s'.()\\-]+(\\._[a-zA-Z0-9\\-]+)+\\.local";


    @Override
    public LogDTO parse(String log) {
        LogDTO logDTO = logMap(log);

        return logDTO;
    }

    // TODO: 인덱스로 파싱하게 되면, 안되지 않을까
    // TODO: 정규 표현식을 match하여 파싱하고, log의 시간과 message를 같이 담는다.
    private LogDTO logMap(String log){
        String[] splited = log.split("\t");

        int[] macIndex = findMatchedIndex(splited, macRegex);
        int[] ipIndex = findMatchedIndex(splited, ipRegex);
        int[] deviceIndex = findMatchedIndex(splited, deviceRegex);
        
        String mac = splited[macIndex[0]];
        String ip = splited[ipIndex[0]];
        String device = validateDeviceName(splited[deviceIndex[0]]);

        return LogDTO.createNew(mac, ip, device);
    }

    private int[] findMatchedIndex(String[] addresses, String regex){
        int[] indexes = new int[2];
        int insert = 0;
        for(int i = 0; i < addresses.length; i++){
            if(addresses[i].matches(regex)){
                indexes[insert] = i;
                insert++;
            }
        }
        return indexes;
    }

    private String validateDeviceName(String old){
        if(old.contains("._rdlink._tcp.local")) return old.replace("._rdlink._tcp.local", "");
        if(old.contains("._companion-link._tcp.local")) return old.replace("._companion-link._tcp.local", "");
        if(old.contains("._airplay._tcp.local")) return old.replace("._airplay._tcp.local", "");
        if(old.contains("._dosvc._tcp.local")) return old.replace("._dosvc._tcp.local", "");
        //TODO: null 반환이 맞나..?

        // 잘못된 파싱으로 인해 mac 주소가 파싱될 경우
        if(old.matches(macRegex)) return null;
        return old;
    }

}
