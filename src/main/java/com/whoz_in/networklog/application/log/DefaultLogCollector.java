package com.whoz_in.networklog.application.log;

import com.whoz_in.networklog.config.ProcessConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
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

    @Getter
    private final Set<String> logInfos = new HashSet<>();

    @Autowired
    public DefaultLogCollector(ProcessConfig config) {
        this.pb = new ProcessBuilder(config.getCommand())
                .redirectErrorStream(true);

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
    public void execute(NetworkLogManager manager) {
        try {
            String line;
            int cnt=0;

            while ((line = br.readLine()) != null) {
                logInfos.add(line);
                cnt++;

                if(cnt>=1000) {
                    manager.callBack(logInfos);
                    cnt=0;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
