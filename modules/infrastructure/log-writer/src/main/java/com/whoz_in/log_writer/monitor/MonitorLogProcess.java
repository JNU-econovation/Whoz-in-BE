package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.common.process.ContinuousProcess;
import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public final class MonitorLogProcess extends ContinuousProcess {

    public MonitorLogProcess(MonitorInfo info, String sudoPassword) {
        try {
            new File("../error").mkdir(); //에러 처리 수정하면 이거 없앨게요..
            super.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(new File("../error", "monitor.txt"))
                    .start();
            super.br = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));

            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(info.command() + " 실행 실패");
        }
    }
}
