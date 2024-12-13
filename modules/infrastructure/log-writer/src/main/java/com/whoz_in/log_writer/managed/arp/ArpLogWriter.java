package com.whoz_in.log_writer.managed.arp;


import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    //주기적으로 arp 명령어를 실행하여 로그를 저장함
    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void scan() {
        List<ManagedLog> logs = arpList.stream() //실행할 arp들을 스트림화
                .map(info -> new ArpLogProcess(info, sudoPassword)) //arp 실행
                .map(this::getLogsFromProcess) //arp 출력을 로그 Set으로 변환
                .flatMap(Collection::stream) //Set끼리 합침
                .toList();
        dao.upsertAll(logs);
    }

    //프로세스의 출력들을 로그로 변환한다.
    private Set<ManagedLog> getLogsFromProcess(ArpLogProcess process){
        Set<ManagedLog> logs = process.resultList().stream()
                .filter(parser::validate)
                .map(parser::parse)
                .collect(Collectors.toSet());//Set으로 중복 제거

        String ssid = process.getInfo().ni().getEssid();
        logs.forEach(log->log.setSsid(ssid));
        log.info("[managed - arp({})] log to save : {}", ssid, logs.size());
        return logs;
    }
}
