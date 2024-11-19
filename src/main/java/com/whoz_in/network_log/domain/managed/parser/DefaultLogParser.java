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

    // TODO: 인덱스로 파싱하게 되면, 안되지 않을까 -> mac ip의 순서가 바뀔 수도 있는데, 정규식보다 차라리 env에서 명령어의 순서를 유지하는 방식이 더 비용이 낮을 것이라고 판단
    // TODO: 정규 표현식을 match하여 파싱하고, log의 시간과 message를 같이 담는다. -> 시간이 너무 오래걸리므로, 기각
    private LogDTO logMap(String log){
        String[] splited = log.split("\t");

        if(splited.length < 3) {
            System.err.println("Invalid log format");
            return null; // 과연 null 반환이 맞는지는 모르겠다.
        }

        String mac = splited[0];
        String ip = splited[1];
        String deviceName = validateDeviceName(splited[2]);

        return LogDTO.createNew(mac, ip, deviceName);
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
        //TODO: null 반환이 맞나..?
        // 잘못된 파싱으로 인해 mac 주소가 파싱될 경우
        if(old.matches(macRegex)) return null;
        return old.replaceAll("\\._[a-zA-Z0-9\\-]+._tcp\\.local", "");
    }

}
