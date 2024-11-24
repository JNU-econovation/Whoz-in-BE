package com.whoz_in.network_log.infra.managed.arp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

//TODO: 얘는 주기적으로 실행해줘야 하는 친구 이므 ArpLogProcess 객체는 하나로 유지하되, 클래스 내부에서 apr-scan 프로세스를 여러번 실행 시키는 방식으로
public class ArpLogProcess {

    private Process process;
    private BufferedReader br;
    private BufferedWriter bw;

    public ArpLogProcess(String command, String passwrod){
        try {
            process = new ProcessBuilder(command.split(" ")).start();
            this.bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            this.bw.write(passwrod);
            this.bw.newLine();
            this.bw.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> start(){
        Set<String> logs = new HashSet<>();

        try{
            String line;
            while((line = br.readLine()) != null){
                logs.add(line);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        return logs;
    }

}
