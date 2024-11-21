package com.whoz_in.network_log.infra.managed.mdns.collector;

import com.whoz_in.network_log.infra.managed.mdns.manager.MulticastDNSLogManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LogProcess {

    private Process process;
    private BufferedReader br;
    private BufferedWriter bw;
    private String password;
    private Object callback;

    public LogProcess(String[] command, Object callback) {
        try {
            this.process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();

            this.callback = callback;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(String password) {
        try {
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
                ((MulticastDNSLogManager) callback).receive(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
