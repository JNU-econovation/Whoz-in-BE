package com.whoz_in.network_log.domain.monitor;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitorLogger {
    @Qualifier("monitorTShark")
    private final Process monitorTShark;
    private final Set<String> macs = new HashSet<>();
    private final MonitorLogDAO repo;
    @PostConstruct
    public void startLogging() {
        new Thread(this::processLogs).start();
    }

    private void processLogs() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(monitorTShark.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("\t");
                if (lineWords.length != 2) return;
                macs.add(lineWords[0]);
                macs.add(lineWords[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("monitor logger: tshark 출력 읽기 실패", e);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void saveLogs(){
        System.out.println("[monitor] 저장할 mac 개수: " + macs.size());
        repo.upsertAll(macs);
    }

}