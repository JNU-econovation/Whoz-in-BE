package com.whoz_in.network_log.domain.managed.collector;

import com.whoz_in.network_log.domain.managed.ManagedConfig;
import com.whoz_in.network_log.domain.managed.manager.LogManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// TODO: 패킷을 넘길 때 필터링을 하는 친구를 만들자.

@Component
public class DefaultLogCollector implements LogCollector {

    private Process process;
    private BufferedWriter bw;
    private BufferedReader br;
    private LogManager logManager;

    @Autowired
    public DefaultLogCollector(ManagedConfig config) {
        String password = config.getPassword();
        Arrays.stream(config.mDnsCommands())
                        .map(command -> command.split(" "))
                        .forEach(command -> new Thread(() -> start(command, password)).start());
    }

    private void start(String[] command, String password) {
        ProcessBuilder pb = new ProcessBuilder(command)
                .redirectErrorStream(true);

        try {
            process = pb.start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()), 64 * 1024);
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()), 64 * 1024);
            bw.write(password);
            bw.newLine();
            bw.flush();
        } catch (IOException e){
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void collect() {
        // 읽어오기
        try {
            String line;

            while ((line = br.readLine()) != null) {
                callBack(line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setManager(LogManager manager) {
        this.logManager = manager;
    }

    private void callBack(Set<String> logInfos) {
        logManager.receive(logInfos);
    }

    private void callBack(String log){ logManager.receive(log); }

}
