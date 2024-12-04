package com.whoz_in.log_writer.common.process;

import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//실행 후 종료되어 모든 출력을 얻는 프로세스
//출력 스트림을 통한 프로세스와의 상호작용은 없다.

public class TransientProcess {
    private static final Logger logger = LoggerFactory.getLogger(TransientProcess.class);

    protected BufferedReader br;
    protected Process process;

    public TransientProcess() {}

    public TransientProcess(Process process){
        this.process = process;
        this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
    public TransientProcess(String command){
        this(command, null);
    }
    public TransientProcess(String command, @Nullable String sudoPassword) {
        try {
            this.process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();
            this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (sudoPassword==null) return;
            Writer writer = new OutputStreamWriter(this.process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //종료되었을 때 출력을 얻는다.
    //종료되지 않았다면 블로킹된다.
    //출력이 없는 프로세스의 경우 빈 리스트를 출력한다.
    public List<String> results(){
        waitTermination();
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

    //결과를 하나의 String으로 반환
    public String resultsInOne(){
        waitTermination();
        StringBuilder sb = new StringBuilder();
        try {
            int size=1024;
            char[] buff = new char[size];
            int read;
            while((read = br.read(buff, 0, size)) != -1){
                sb.append(buff, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public void waitTermination(){
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            logger.error("인터럽트됨");
        }
    }
}
