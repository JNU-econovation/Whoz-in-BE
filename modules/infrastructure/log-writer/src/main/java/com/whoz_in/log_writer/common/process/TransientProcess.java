package com.whoz_in.log_writer.common.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

//실행 후 종료되어 모든 출력을 얻는 프로세스
//출력 스트림을 통한 프로세스와의 상호작용은 없다.

public class TransientProcess {

    protected BufferedReader br;
    protected BufferedReader ebr;
    protected Process process;

    public TransientProcess() {}

    // sudo 없이 실행할 커맨드
    // command 예시: "ifconfig"
    // IOException을 여기서 처리하므로 무조건 실행되어야 하는 명령어는 사용하면 안된다!
    public TransientProcess(String command){
        try {
            this.process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(command+" - 알 수 없는 원인으로 실행에 실패했습니다.");
        }
        this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    // sudo로 실행할 커맨드는 비밀번호 입력이 필요하여 나눠진 것
    // sudoCommand 예시: "sudo -S iwconfig"
    // IOException을 여기서 처리하므로 무조건 실행되어야 하는 명령어는 사용하면 안된다!
    public TransientProcess(String sudoCommand, String sudoPassword) {
        this(sudoCommand);
        /*
        output stream에 쓰기 전에 종료될 가능성이 있는 경우
        1. sudo로 실행을 안했을때
        2. (리눅스) groups에 포함된 사용자로 실행했을 때
        */
        if (!this.process.isAlive()) return;
        Writer writer = new OutputStreamWriter(this.process.getOutputStream());
        try {
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(sudoCommand + " - sudo 명령어 입력 중 오류 발생");
        }
    }

    public String resultString(){
        return resultString(this.br);
    }

    public List<String> resultList(){
        return resultList(this.br);
    }

    public String errorResultString(){
        return resultString(this.ebr);
    }

    public List<String> errorResultList(){
        return resultList(this.ebr);
    }

    //종료되었을 때 출력을 얻는다.
    //종료되지 않았다면 블로킹된다.
    //출력이 없는 프로세스의 경우 빈 리스트를 출력한다.
    private List<String> resultList(BufferedReader br){
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
    private String resultString(BufferedReader br){
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
            throw new RuntimeException(e);
        }
    }
}
