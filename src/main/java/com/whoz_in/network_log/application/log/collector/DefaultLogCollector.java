package com.whoz_in.network_log.application.log.collector;

import com.whoz_in.network_log.application.log.ManagerBeanFinder;
import com.whoz_in.network_log.application.log.manager.LogManager;
import com.whoz_in.network_log.config.ProcessConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: 이 클래스를 AbstractAddressProcess로 만든 이유는, 네트워클 정보를 읽어오는 수단이 꼭 tShark가 아닐 수도 있기 떄문에

@Component
public class DefaultLogCollector implements LogCollector {

    private final ProcessBuilder pb;
    private BufferedWriter bw;
    private BufferedReader br;
    private ManagerBeanFinder managerFinder;

    @Getter
    private final Set<String> logInfos = new HashSet<>();

    @Autowired
    public DefaultLogCollector(ProcessConfig config,
                               ManagerBeanFinder managerFinder ) {
        this.pb = new ProcessBuilder(config.mDnsCommand())
                .redirectErrorStream(true);
        this.managerFinder = managerFinder;

        try{
            Process process = pb.start();
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public DefaultLogCollector(String command) {
        this.pb = new ProcessBuilder(command.split(" "));
    }

    @Override
    public void collect() {
        try {
            String line;
            int cnt=0;

            while ((line = br.readLine()) != null) {
                logInfos.add(line);
                cnt++;

                if(cnt>=1000) {
                    callBack(logInfos);
                    cnt=0;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void callBack(Set<String> logInfos) {
        Map<String, LogManager> managers = managerFinder.getManagers();

        managers.keySet()
                .forEach(key -> managers.get(key).callBack(logInfos));

    }

}
