package com.whoz_in.log_writer.managed.arp;


import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.config.NetworkConfig.Managed;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArpLogWriter {
    private final ManagedLogDAO dao;
    private final ArpLogParser parser;
    private final List<Managed> arpList;
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

    @Scheduled(fixedRate = 5000)
    private void scan() {
        Set<ManagedLog> logs= arpList.stream()
                .flatMap(arp-> {
                    ArpLogProcess proc = new ArpLogProcess(arp.command(), sudoPassword, arp.ssid()); //프로세스 실행
                    List<String> lines = proc.readLines(); //프로세스의 모든 출력 가져오기
                    proc.printError();
                    return lines.stream() //출력 라인들을 ManagedLog 변환하며 ssid도 넣어줌
                            .filter(parser::validate)
                            .map(line->{
                                ManagedLog log = parser.parse(line);
                                log.setSsid(arp.ssid());
                                return log;
                            });
                })
                .collect(Collectors.toSet()); //Set으로 중복 제거
        System.out.println("[managed - arp] 저장할 로그 개수 : " +logs.size());
        dao.insertAll(logs);
    }
}
