package com.whoz_in.log_writer.managed.arp;

import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//단발성 프로세스
public class ArpLogProcess {
    private final BufferedReader br;
    private final Process process;
    public ArpLogProcess(ManagedInfo info, String password) {
        try {
            //TODO: error 처리 수정
            this.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(new File("../error", info.ssid()+".txt"))
                    .start();
            this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bw.write(password);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //단발성 프로세스로, 프로세스가 종료될 때까지 run은 블로킹된다.
    public List<String> readLines(){
        List<String> logs = new ArrayList<>();

        try {
            String line;
            while((line = br.readLine()) != null){
                logs.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return logs;
    }

    public boolean isAlive(){
        return process.isAlive();
    }
}
