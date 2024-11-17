package com.whoz_in.network_log.domain.monitor;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitorLogger {
    @Qualifier("monitorTShark")
    private final Process monitorTShark;
    private final MonitorLogRepository repo;

    @PostConstruct
    public void startLogging() {
        new Thread(this::processLogs).start();
    }

    private void processLogs() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(monitorTShark.getInputStream()), 100)) {
            Stream<String> lines;
            while ((lines = reader.lines()) != null) {
                lines.filter(line -> {
                    String[] ipAndMac = line.split("\t");
                    //ip, mac이 맞는지 확인하는 로직. 간단하게 작성함
                    return (ipAndMac.length == 2 && !ipAndMac[0].isEmpty() && !ipAndMac[1].isEmpty());
                }).map(line-> {
                    String[] ipAndMac = line.split("\t");
                    return new MonitorLog(ipAndMac[1], ipAndMac[0]);
                }).forEach(repo::save);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("monitor logger: tshark 출력 읽기 실패", e);
        }
    }

    private void processLine(String line) {
        // Tshark 출력 데이터 파싱
        System.out.println(line);
        //배치 처리
    }

}