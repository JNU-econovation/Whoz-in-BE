package com.whoz_in.log_writer.infra.managed.arp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArpLogProcess {

    private final String command;
    private final String password;

    /**
     *
     * @param command
     * @param password
     * 단발성인 프로세스이므로 command 와 sudo password 를 알고 있으면 된다.
     */
    public ArpLogProcess(
            @Value("${network.process.command.managed.arp}") final String command,
            @Value("${network.process.password}") String password){
        this.command = command;
        this.password = password;
    }

    // 단발성 프로세스 이므로, start 를 호출하면 arp 로그를 받아오는 프로세스를 실행
    public Set<String> start(){
        Set<String> logs = new HashSet<>();

        try {
            Process process = new ProcessBuilder(command.split(" ")).start();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            bw.write(password);
            bw.newLine();
            bw.flush();

            String line;

            while((line = br.readLine()) != null){
                logs.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return logs;
    }

}
