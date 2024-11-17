package com.whoz_in.network_log.application.log.parser;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

// TODO: 테스트하기
@Component
public class DefaultLogParser implements LogParser{

    // TODO: Regex가 맞는지 검증해야 함
    private final String macRegex = "[0-9A-z]{2}:[A-z0-9]{2}:[A-z0-9]{2}:[A-z0-9]{2}:[A-z0-9]{2}:[A-z0-9]{2}";
    private final String ipRegex = "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)";
    private final String timeRegex = "[A-Z][a-z]{2} \\d{1,2}, \\d{4} \\d{2}:\\d{2}:\\d{2}\\.\\d{6,9} [A-Z]{3}";

    @Override
    public Map<String, String> parse(String log) {
        Map<String, String> logMap = logMap(log);

        return logMap;
    }

    // TODO: 인덱스로 파싱하게 되면, 안되지 않을까
    // TODO: 정규 표현식을 match하여 파싱하고, log의 시간과 message를 같이 담는다.
    private Map<String, String> logMap(String log){
        Map<String, String> logMap = new HashMap<>();
        String[] splited = log.split("\t");

        int[] macIndex = findMatchedIndex(splited, macRegex);
        int[] ipIndex = findMatchedIndex(splited, ipRegex);
        int[] timeIndex = findMatchedIndex(splited, timeRegex);
        
        logMap.put("src_mac", splited[macIndex[0]]);
        logMap.put("src_ip", splited[ipIndex[0]]);
        logMap.put("dst_mac", splited[macIndex[1]]);
        logMap.put("dst_ip", splited[ipIndex[1]]);
        logMap.put("time", splited[timeIndex[0]]);

        return logMap;
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

}
