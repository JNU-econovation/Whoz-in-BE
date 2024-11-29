package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.config.NetworkConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorLogWriter {
    private MonitorLogProcess process; //교체될 수 있으므로 final X
    private final MonitorLogParser parser;
    private final MonitorLogDAO repo;
    private final String sudoPassword;
    private final MonitorInfo monitorInfo;

    public MonitorLogWriter(MonitorLogParser parser, MonitorLogDAO repo, NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.parser = parser;
        this.repo = repo;
        this.monitorInfo = config.getMonitorInfo();
        this.sudoPassword = sudoPassword;
        this.process = new MonitorLogProcess(monitorInfo, sudoPassword);
    }
    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    private void saveLogs(){
        if (!process.isAlive()) {
            System.err.println("[monitor] 종료됨 : ERROR");
            return;
        }
        Set<String> macs = new HashSet<>();
        String line;
        for(;;) {
            line = process.readLine();
            if (line == null) break;
            macs.addAll(parser.parse(line));
        }
        macs.remove("");

        System.out.println("[monitor] 저장할 mac 개수: " + macs.size());
        repo.upsertAll(macs);
        macs.clear();
    }

}