package com.whoz_in.log_writer.infra.monitor;

import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public final class MonitorLogProcess {
    private final Process process;
    private final NonBlockingBufferedReader cbr;

    public MonitorLogProcess(String command, String sudoPassword) {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        try {
            process = pb.start();
            this.cbr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));

            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(command + " 실행 실패");
        }
    }

    public String readLine() throws IOException {
        return cbr.readLine();
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }

    public void destory(){
        this.process.destroy();
    }
}
