package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.config.NetworkConfig;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorLogWriter {
    private MonitorLogProcess process; //교체될 수 있으므로 final X
    private Boolean wasDead = false;
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
        log.info("[monitor] started");
    }
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void saveLogs(){
        if (!process.isAlive()) {
            if (wasDead.equals(Boolean.FALSE)) {
                log.error("[monitor] dead :\n{}\n{}", "에러 스트림 내용:", process.readErrorLines());
                wasDead = true;
            }
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

        log.info("[monitor] mac to save: " + macs.size());
        repo.upsertAll(macs);
    }

}