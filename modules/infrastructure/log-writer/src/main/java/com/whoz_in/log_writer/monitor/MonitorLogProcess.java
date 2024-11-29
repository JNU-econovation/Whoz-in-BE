package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public final class MonitorLogProcess {
    private final Process process;
    private final NonBlockingBufferedReader cbr;

    public MonitorLogProcess(MonitorInfo info, String sudoPassword) {
        try {
            new File("../error").mkdir(); //에러 처리 수정하면 이거 없앨게요..
            process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(new File("../error", "monitor.txt"))
                    .start();
            this.cbr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));

            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(info.command() + " 실행 실패");
        }
    }

    public String readLine(){
        try {
            return cbr.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }

    public void destory(){
        this.process.destroy();
    }
}
