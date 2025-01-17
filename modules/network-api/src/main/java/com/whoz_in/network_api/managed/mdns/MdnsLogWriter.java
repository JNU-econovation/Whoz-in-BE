package com.whoz_in.network_api.managed.mdns;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.network_api.common.NetworkInterface;
import com.whoz_in.network_api.common.SystemNetworkInterfaces;
import com.whoz_in.network_api.config.NetworkConfig;
import com.whoz_in.network_api.managed.ParsedLog;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//TODO: tshark가 종료되지 않더라도 오류가 발생할 수 있으려나? 이 경우에도 로깅이 필요하긴 함
@Slf4j
@Component
public class MdnsLogWriter {
    private final String room;
    private final Map<NetworkInterface, MdnsLogProcess> processes;
    private final Map<NetworkInterface, Boolean> dead;
    private final MdnsLogParser parser;
    private final ManagedLogRepository repository;
    private final String sudoPassword;
    private final SystemNetworkInterfaces systemNIs;

    public MdnsLogWriter(ManagedLogRepository repository, MdnsLogParser parser, NetworkConfig config,
            SystemNetworkInterfaces systemNIs
    ) {
        this.room = config.getRoom();
        this.repository = repository;
        this.parser = parser;
        this.sudoPassword = config.getSudoPassword();
        this.processes = new HashMap<>();
        this.dead = new HashMap<>();
        this.systemNIs = systemNIs;
        config.getMdnsNIs().forEach(this::startProcess);
    }

    //주기적으로 로그를 저장함

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void saveLogs() {
        List<ManagedLog> totalLogs = this.processes.entrySet().parallelStream()
                .filter(entry -> entry.getValue().isAlive())
                .map(entry -> getLogsFromProcess(entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream)
                .toList();
        repository.saveAll(totalLogs);
    }

    private List<ManagedLog> getLogsFromProcess(NetworkInterface ni, MdnsLogProcess process) {
        Set<ParsedLog> logs = process.readLines().stream()
                .map(parser::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        String altSsid = ni.getAltSsid();
        log.info("[managed - mdns({})] log to save : {}", altSsid, logs.size());

        //Parsed를 Managed로 변환
        return logs.stream()
                .map(log -> new ManagedLog(
                        log.getMac(), log.getIp(),
                        log.getDeviceName(), altSsid, room,
                        log.getCreatedAt(), log.getCreatedAt()
                ))
                .toList();
    }

    //주기적으로 프로세스가 죽었는지 확인하고 재실행함
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void checkProcesses(){
        this.processes.entrySet().parallelStream()
                .filter(entry-> !entry.getValue().isAlive())
                .forEach(entry ->{
                    NetworkInterface ni = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    //프로세스가 죽었다는 것을 인지했을때만 아래 로직을 실행
                    if (dead.get(ni).equals(Boolean.FALSE)) {
                        dead.put(ni, true);
                        log.error("[managed - mdns({})] 프로세스가 종료됨 :\n{}\n{}", ni.getRealSsid(), "프로세스의 에러 스트림 내용:", process.readErrorLines());
                    }
                    startProcess(ni);
                });
    }

    private void startProcess(NetworkInterface ni){
        String altSsid = ni.getAltSsid();
        log.info("[managed - mdns({})] 프로세스를 실행합니다.", altSsid);

        if (!systemNIs.exists(ni)){
            log.error("[managed - mdns({})] 설정된 mdns 네트워크 인터페이스가 시스템에 존재하지 않습니다.", altSsid);
            return;
        }
        Optional.ofNullable(this.processes.get(ni)).ifPresent(MdnsLogProcess::terminate);
        MdnsLogProcess process = new MdnsLogProcess(ni.getCommand(), sudoPassword);
        this.processes.put(ni, process);
        this.dead.put(ni, false);

        log.info("[managed - mdns({})] 프로세스 실행 완료", altSsid);
    }
}
