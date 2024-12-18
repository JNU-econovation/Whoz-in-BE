package com.whoz_in.log_writer.common.process;

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
@Slf4j
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
            throw new RuntimeException("[Transient Process] " + command + " : 실행 실패");
        }
        this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    // sudo로 실행할 커맨드는 비밀번호 입력이 필요하여 나눠진 것
    // sudoCommand 예시: "sudo -S iwconfig"
    // IOException을 여기서 처리하므로 무조건 실행되어야 하는 명령어는 사용하면 안된다!
    public TransientProcess(String sudoCommand, String sudoPassword) {
        this(sudoCommand);
        Writer writer = new OutputStreamWriter(this.process.getOutputStream());
        try {
            /*
            writer를 통해 password를 입력하기 전에 프로세스가 종료됐을경우 예외가 뜬다.
            프로세스가 빨리 끝나는 두 가지 경우의 수가 있다. (정상적인 상황이라고 가정)
            1. sudo로 실행을 안했을때
            2. (리눅스) groups에 포함된 사용자로 실행했을때
            이를 아래에서 catch한다.
            */
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            if (e.getMessage().contains("Broken pipe") && //프로세스가 종료됐거나 연결이 끊겨서 발생한 예외인지
                    !this.process.isAlive() && //프로세스가 종료됐는지
                    this.process.exitValue() == 0 //프로세스가 정상적으로 종료됐는지
            ) {
                //정상적으로 실행됐다고 판단하여 warn 로깅
                log.warn("[Transient Process] {} : sudo 비밀번호 입력 전에 프로세스 종료됨", sudoCommand);
                return;
            }
            //이외는 예외를 띄운다.
            throw new RuntimeException("[Transient Process] " + sudoCommand + " : sudo 명령어 입력 중 오류 발생" + "\ninput stream:" + resultString() + "\nerror stream:" + errorResultString());
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
