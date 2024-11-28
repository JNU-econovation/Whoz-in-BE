package com.whoz_in.log_writer.managed.arp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.annotation.Async;

//단발성 프로세스
public class ArpLogProcess {
    private final BufferedReader br;
    private final BufferedReader ebr;
    private final Process process;
    private final String ssid;
    public ArpLogProcess(String command, String password, String ssid) {
        this.ssid = ssid;
        try {
            this.process = new ProcessBuilder(command.split(" ")).start();
            this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bw.write(password);
            bw.newLine();
            bw.flush();
            //참고: 종료된 프로세스에서도 Input, Error 스트림은 사용 가능. Output에 접근 시 broken pipe 에러.
            this.ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
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

    @Async
    public void printError(){
        try {
            String errorLine = ebr.readLine();
            System.err.println("[managed - arp] %s".formatted(this.ssid));
            System.err.println("\t"+errorLine);
            while((errorLine = ebr.readLine()) != null){
                System.err.println("\t"+errorLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
