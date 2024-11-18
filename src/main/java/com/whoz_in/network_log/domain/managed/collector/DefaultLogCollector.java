package com.whoz_in.network_log.domain.managed.collector;

import com.whoz_in.network_log.domain.managed.ProcessConfig;
import com.whoz_in.network_log.domain.managed.manager.LogManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// TODO: 이 클래스를 AbstractAddressProcess로 만든 이유는, 네트워클 정보를 읽어오는 수단이 꼭 tShark가 아닐 수도 있기 떄문에

@Component
public class DefaultLogCollector implements LogCollector {

    private final ProcessBuilder pb;
    private Process process;
    private BufferedWriter bw;
    private BufferedReader br;
    private LogManager logManager;

    @Getter
    private final Set<String> logInfos = new HashSet<>();

    @Autowired
    public DefaultLogCollector(ProcessConfig config) {
        this.pb = new ProcessBuilder(config.mDnsCommand())
                .redirectErrorStream(true);
    }

    public DefaultLogCollector(String command) {
        this.pb = new ProcessBuilder(command.split(" "));
    }

    @Override
    @Async
    public void collect() {
        execute();
    }

    @Override
    public void setManager(LogManager manager) {
        this.logManager = manager;
    }

    private void execute(){
        // 프로세스 초기화
        try {
            process = pb.start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e){
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

        // 읽어오기
        try {
            String line;

           while ((line = br.readLine()) != null) {
               if(line.contains("Capturing on")) continue;
               logInfos.add(line);
               callBack(new HashSet<>(logInfos));
           }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void callBack(Set<String> logInfos) {
        logManager.receive(logInfos);
    }

}
