package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.process.TransientProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public final class IpRuleManager {

    // 'ip rule list' 출력에서 'from <IP> lookup <TABLE>' 형태를 파싱하는 정규표현식
    private static final Pattern RULE_PATTERN = Pattern.compile("^(\\d+):\\s+from\\s+([\\d.]+)\\s+lookup\\s+(\\S+).*");

    // 정규표현식으로 걸러내어 Map<ip, table> 형태로 반환
    public Map<String, String> getRules() {
        Map<String, String> ruleMap = new HashMap<>();
        List<String> lines = TransientProcess.create("ip rule list").results();
        for (String line : lines) {
            Matcher matcher = RULE_PATTERN.matcher(line);
            if (matcher.matches()) {
                String ip = matcher.group(2);
                String table = matcher.group(3);
                ruleMap.put(ip, table);
            }
        }
        return ruleMap;
    }

    // 지정된 테이블 이름을 가진 ip rule을 모두 삭제
    public void deleteRuleByTable(String tableName) {
        Map<String, String> rules = getRules();
        rules.forEach((ip, table) -> {
            if (table.equals(tableName)) {
                String delCmd = String.format("sudo ip rule del from %s table %s", ip, tableName);
                log.info("Deleting ip rule: {}", delCmd);
                TransientProcess.create(delCmd).waitTermination();
            }
        });
    }

    // IP와 테이블 이름으로 ip rule을 추가
    public void addRule(String ip, String table) {
        String addCmd = String.format("sudo ip rule add from %s table %s", ip, table);
        TransientProcess.create(addCmd).waitTermination();
        log.info("ip rule 추가됨 : {} - {}", ip, table);
    }

    // getRules()로 파싱된 모든 ip rule(사용자 정의 규칙)을 삭제
    public void deleteAllRules() {
        Map<String, String> rules = getRules();
        rules.forEach((ip, table) -> {
            String delCmd = String.format("sudo ip rule del from %s table %s", ip, table);
            TransientProcess.create(delCmd).waitTermination();
        });
        log.info("ip rule 초기화됨");
    }
}

