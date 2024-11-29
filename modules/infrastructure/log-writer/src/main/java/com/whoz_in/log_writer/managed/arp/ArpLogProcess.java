package com.whoz_in.log_writer.managed.arp;

import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ArpLogProcess extends TransientProcess {
    private final BufferedReader br;
    private final Process process;
    public ArpLogProcess(ManagedInfo info, String password) {
        try {
            //TODO: error 처리 로직 수정
            new File("../error").mkdir(); //에러 처리 수정하면 이거 없앨게요..
            this.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(new File("../error", info.ssid()+".txt")) //이것도..
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


    @Override
    public List<String> results(){
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
}
