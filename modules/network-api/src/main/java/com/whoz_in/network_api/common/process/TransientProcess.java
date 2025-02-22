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
@Slf4j
public class TransientProcess extends AbstractProcess{
    protected BufferedReader br;
    protected BufferedReader ebr;

    private TransientProcess(ProcessCommand command, Process process){
        super(command, process);
    }

    public static TransientProcess start(String command){
        return TransientProcess.start(ProcessCommand.of(command));
    }

    // 프로세스를 실행하며, sudo로 실행됐다면 비밀번호를 입력한다.
    // IOException을 여기서 처리하므로 무조건 실행되어야 하는 명령어는 사용하면 안된다!
    public static TransientProcess start(ProcessCommand command){
        Process process;
        try {
            process = new ProcessBuilder(command.getCommand())
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException("[Transient Process] " + command + " 실행 실패 - " + e.getMessage());
        }
        TransientProcess tp = new TransientProcess(command, process);
        tp.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        tp.ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        if (command.isSudoCommand()) tp.enterSudoPassword();
        return tp;
    }

    @Override
    protected void enterSudoPassword(){
        try {
            /*
            writer를 통해 password를 입력하기 전에 프로세스가 종료됐을경우 예외가 뜬다.
            프로세스가 빨리 종료되는 두 가지 경우의 수가 있다. (정상적인 상황이라고 가정)
            1. sudo로 실행을 안했을때
            2. (리눅스) groups에 포함된 사용자로 실행했을때
            이를 아래에서 catch한다.
            */
            super.enterSudoPassword();
        } catch (IOException e) {
            if (e.getMessage().contains("Broken pipe") && //프로세스가 종료됐거나 연결이 끊겨서 발생한 예외인지
                    !this.process.isAlive() && //프로세스가 종료됐는지
                    this.process.exitValue() == 0 //프로세스가 정상적으로 종료됐는지
            ) {
                //정상적으로 실행됐다고 판단하여 warn 로깅
                log.warn("[Transient Process] {} : sudo 비밀번호 입력 전에 프로세스 종료됨", command);
                return;
            }
            //이외는 예외를 띄운다.
            throw new RuntimeException("[Transient Process] " + command + " : sudo 비밀번호 입력 실패" + "\ninput stream:" + result() + "\nerror stream:" + errorResult());
        }
    }


    public String result(){
        return result(this.br);
    }

    public List<String> results(){
        return results(this.br);
    }

    public String errorResult(){
        return result(this.ebr);
    }

    public List<String> errorResults(){
        return results(this.ebr);
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

    public void waitTermination(){
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
