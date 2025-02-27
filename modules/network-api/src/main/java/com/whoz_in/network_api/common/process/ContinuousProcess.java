package com.whoz_in.network_api.common.process;

import com.whoz_in.network_api.common.util.NonBlockingBufferedReader;
import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

//실행 후 종료되지 않는 프로세스
//꾸준히 출력을 읽을 수 있어야 한다.
public class ContinuousProcess extends AbstractProcess{
    protected NonBlockingBufferedReader br;
    //기본 생성자를 호출했을 땐 ebr이 null일 수 있다.
    @Nullable
    protected NonBlockingBufferedReader ebr = null;

    private ContinuousProcess(ProcessCommand command, Process process){
        super(command, process);
    }
    public static ContinuousProcess start(String command){
        return ContinuousProcess.start(ProcessCommand.of(command));
    }
    public static ContinuousProcess start(ProcessCommand command){
        Process process;
        try {
            process = new ProcessBuilder(command.getCommand()).start();
        } catch (IOException e) {
            throw new RuntimeException(command + " 실행 실패");
        }
        ContinuousProcess cp = new ContinuousProcess(command, process);
        cp.br = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));
        cp.ebr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getErrorStream())));
        if (command.isSudoCommand()) cp.enterSudoPassword();
        return cp;
    }

    @Override
    protected void enterSudoPassword(){
        try {
            super.enterSudoPassword();
        } catch (IOException e) {
            throw new RuntimeException("[Continuous Process] " + command + " : sudo password 입력 실패 - " + e.getMessage());
        }
    }

    /**
     * @return 프로세스의 출력에서 한 줄을 읽어들인다.
     * 읽을 줄이 없을경우 null을 출력한다.
     */
    public String readLine(){
        try {
            return this.br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try {
            String line;
            while((line=this.br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return 프로세스의 에러 출력에서 한 줄을 읽어들인다.
     * 읽을 줄이 없을경우 null을 출력한다.
     */
    public String readErrorLine(){
        if (this.ebr == null)
            throw new RuntimeException("error stream을 초기화하지 않았습니다!");
        try {
            return this.ebr.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readErrorLines(){
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = readErrorLine()) != null){
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }
    public void terminate(){
        this.process.destroy();
    }
}
