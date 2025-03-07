package com.whoz_in.network_api.common.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

//실행 후 종료되어 모든 출력을 얻는 프로세스
//출력 스트림을 통한 프로세스와의 상호작용은 없다.
public class TransientProcess extends AbstractProcess{

    TransientProcess(String command) {
        super(command);
    }

    public static TransientProcess create(String command) {
        TransientProcess transientProcess = new TransientProcess(command);
        transientProcess.start();
        return transientProcess;
    }

    public String result(){
        return result(super.outputReader);
    }

    public List<String> results(){
        return results(super.outputReader);
    }

    public String errorResult(){
        return result(super.errorReader);
    }

    public List<String> errorResults(){
        return results(super.errorReader);
    }

    public void waitTermination(){
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //종료되었을 때 출력을 얻는다.
    //종료되지 않았다면 블로킹된다.
    //출력이 없는 프로세스의 경우 빈 리스트를 출력한다.
    private List<String> results(BufferedReader br){
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
    private String result(BufferedReader br){
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
}
