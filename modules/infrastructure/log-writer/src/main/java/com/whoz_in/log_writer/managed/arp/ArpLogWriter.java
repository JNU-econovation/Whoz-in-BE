package com.whoz_in.log_writer.managed.arp;


import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                    List<String> lines = proc.results(); //프로세스의 모든 출력 가져오기
                    Set<ManagedLog> procLogs = lines.stream() //출력 라인들을 ManagedLog 변환하며 ssid도 넣어줌
                            .filter(parser::validate)
                            .map(line->{
                                ManagedLog log = parser.parse(line);
                                log.setSsid(arpInfo.ssid());
                                return log;
                            })
                            .collect(Collectors.toSet()); //Set으로 중복 제거

                    /*
                    Arp-scan은 단발성인데
                    Process의 isAlive()는 실행 중일 때도 false일 수 있고, 종료 중일 때도 true일 수 있으므로 오류의 판단이 힘들다.
                    따라서 Arp-scan의 경우 무조건 1개 이상의 결과가 나오므로 0개라면 실행 실패라고 판단한다.
                     */
                    if (procLogs.isEmpty()) {
                        System.err.println("[managed - arp(%s)] 실행 실패 : ERROR".formatted(arpInfo.ssid()));
                        return Stream.empty();
                    }
                    System.out.println("[managed - arp(%s)] 저장할 로그 개수 : %d".formatted(arpInfo.ssid(), procLogs.size()));
                    return procLogs.stream();
                })
                .toList();

        dao.insertAll(logs);
    }
}
