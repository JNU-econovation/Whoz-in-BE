package com.whoz_in.log_writer.common.process;

import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

//실행 후 종료되지 않는 프로세스
//꾸준히 출력을 읽을 수 있어야 한다.
public class ContinuousProcess {
    protected Process process;
    protected NonBlockingBufferedReader br;
    //기본 생성자를 호출했을 땐 ebr이 null일 수 있다.
    @Nullable
    protected NonBlockingBufferedReader ebr = null;

    //sub class에서 자신만의 Process를 정의하고 싶을 때 사용
    //super()는 생성자의 첫 번째 줄에 있어야 하기 때문에 만든 것임
    public ContinuousProcess() {}

    public ContinuousProcess(String command) {
        this(command, null);
    }
    public ContinuousProcess(String command, @Nullable String sudoPassword) {
        try {
            this.process = new ProcessBuilder(command.split(" "))
                    .start();
            this.br = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(this.process.getInputStream())));
            this.ebr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(this.process.getErrorStream())));
            if (sudoPassword==null) return;
            Writer writer = new OutputStreamWriter(this.process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(command + " 실행 실패");
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

    public boolean isAlive(){
        return this.process.isAlive();
    }
    public void terminate(){
        this.process.destroy();
    }
}
