package com.whoz_in.network_api.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.network_api.common.network_interface.InterfaceModeChecker;
import com.whoz_in.network_api.common.process.TsharkProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.system.MonitorModeEnabledEvent;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorLogWriter {
    private TsharkProcess process;
    private final String room;
    private final MonitorLogParser parser;
    private final MonitorLogRepository  repository;
    private final InterfaceModeChecker modeChecker;
    private final NetworkInterfaceProfileConfig config;

    public MonitorLogWriter(@Value("${room-name}") String room, MonitorLogParser parser, MonitorLogRepository repository, NetworkInterfaceProfileConfig config, InterfaceModeChecker modeChecker) {
        this.parser = parser;
        this.repository = repository;
        this.room = room;
        this.config = config;
        this.modeChecker = modeChecker;
    }

    @EventListener
    public void onMonitorModeEnabled(MonitorModeEnabledEvent event) {
        NetworkInterfaceProfile profile = config.getMonitorProfile();

        // 다른 인터페이스의 이벤트면 무시
        if (!profile.interfaceName().equals(event.interfaceName())) {
            return;
        }

        if (process != null && process.isAlive()) return;

        // 첫 시작 또는 종료된 프로세스 재생성
        try {
            log.info("[monitor] {} 모니터 모드 활성화됨. TsharkProcess 생성 시작", event.interfaceName());
            this.process = TsharkProcess.create(
                    profile.command(),
                    profile.interfaceName(),
                    modeChecker
            );
            log.info("[monitor] TsharkProcess 생성 완료");
        } catch (Exception e) {
            log.error("[monitor] TsharkProcess 생성 실패: {}", e.getMessage());
            // 애플리케이션은 계속 실행, 다음 이벤트나 자동 복구를 기다림
        }
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 3000)
    private void saveLogs(){
        //프로세스 죽었으면 기록 안함
        if (process == null || !process.isAlive()) return;

        Set<String> macs = new HashSet<>();
        process.readLines().stream()
                .map(parser::parse)
                .forEach(macs::addAll);
        macs.remove("");
        log.info("[monitor] mac to save: " + macs.size());
        repository.saveAll(macs.stream().map(mac -> new MonitorLog(mac, room)).toList());
    }

    // 오랫동안 켜진 tshark는 패킷을 제대로 잡지 못하는것으로 확인되어 오전 6시에 재실행한다.
    @Scheduled(cron = "0 0 6 * * *")
    private void refreshTshark(){
        if (process == null || !this.process.isAlive()) return;
        this.process.restart();
        log.info("[monitor] tshark가 재실행되었습니다.");
    }
}
