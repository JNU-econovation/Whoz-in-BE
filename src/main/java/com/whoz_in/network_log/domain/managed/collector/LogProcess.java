package com.whoz_in.network_log.domain.managed.collector;

import com.whoz_in.network_log.domain.managed.manager.MulticastDNSLogManager;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LogProcess {

    private ProcessBuilder pb;
    private BufferedReader br;
    private BufferedWriter bw;
    private Process process;
    private String password;
    private Object callback;

    public LogProcess(String[] command, Object callback) {
        this.pb = new ProcessBuilder(command)
                            .redirectErrorStream(true);

        this.callback = callback;
    }

    public void start(String password) {
        try {
            this.process = pb.start();

            br = new BufferedReader(new InputStreamReader(process.getInputStream()), 64 * 1024);
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()), 64 * 1024);

            bw.write(password);
            bw.newLine();
            bw.flush();

            readLog();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readLog(){
        String line;
        try{
            while((line = br.readLine())!=null){
                System.out.println(line);
                ((MulticastDNSLogManager) callback).receive(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
