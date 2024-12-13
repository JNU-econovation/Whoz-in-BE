package com.whoz_in.log_writer.managed.arp;


import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//TODO: 에러 로그 어떻게 관리할지 생각. 일단 TransientProcess라서 구현 안함
@Slf4j
@Component
public class ArpLogWriter {
    private final ManagedLogDAO dao;
    private final ArpLogParser parser;
    private final List<ManagedInfo> arpList;
    private final String sudoPassword;

    public ArpLogWriter(ManagedLogDAO dao,
                        ArpLogParser parser,
                        NetworkConfig arpList,
                        @Value("${sudo_password}") String sudoPassword
            ) {
        this.dao = dao;
        this.parser = parser;
        this.arpList = arpList.getArpList();
        this.sudoPassword = sudoPassword;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void scan() {
        List<ManagedLog> logs= arpList.stream()
                .flatMap(arpInfo-> {
                    ArpLogProcess proc = new ArpLogProcess(arpInfo, sudoPassword); //프로세스 실행
                    List<String> lines = proc.resultList(); //프로세스의 모든 출력 가져오기
                    Set<ManagedLog> procLogs = lines.stream() //출력 라인들을 ManagedLog 변환하며 ssid도 넣어줌
                            .filter(parser::validate)
                            .map(line->{
                                ManagedLog log = parser.parse(line);
                                log.setSsid(arpInfo.ni().getEssid());
                                return log;
                            })
                            .collect(Collectors.toSet()); //Set으로 중복 제거

                    /*
                    Arp-scan은 단발성인데
                    Process의 isAlive()는 실행 중일 때도 false일 수 있고, 종료 중일 때도 true일 수 있으므로 오류의 판단이 힘들다.
                    따라서 Arp-scan의 경우 무조건 1개 이상의 결과가 나오므로 0개라면 실행 실패라고 판단한다.
                     */
                    if (procLogs.isEmpty()) {
                        //SystemValidator가 시스템의 네트워크 인터페이스가 올바른지 검증하기 때문에 여기서는 warn으로 로깅
                        log.warn("[managed - arp({})] 실행 실패 : ERROR", arpInfo.ni().getEssid());
                        return Stream.empty();
                    }
                    log.info("[managed - arp({})] log to save : {}", arpInfo.ni().getEssid(), procLogs.size());
                    return procLogs.stream();
                })
                .toList();

        dao.upsertAll(logs);
    }
}
