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
    ContinuousProcess(String command){
        super(command);
    }

    public static ContinuousProcess create(String command) {
        ContinuousProcess continuousProcess = new ContinuousProcess(command);
        continuousProcess.start();
        return continuousProcess;
    }

    @Override
    protected void init() throws IOException {
        super.process = new ProcessBuilder(command.getCommand()).start();
        super.outputReader = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));
        super.errorReader = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getErrorStream())));
    }

    // 읽을 줄이 없을경우 null을 출력한다.
    public String readLine(){
        try {
            return super.outputReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try {
            String line;
            while((line=super.outputReader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 읽을 줄이 없을경우 null을 출력한다.
    public String readErrorLine(){
        if (super.errorReader == null)
            throw new IllegalStateException("error stream을 초기화하지 않았습니다!");
        try {
            return super.errorReader.readLine();
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
}
