package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.common.process.ContinuousProcess;
import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MonitorLogProcess extends ContinuousProcess {

    public MonitorLogProcess(MonitorInfo info, String sudoPassword) {
        try {
            super.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(Redirect.INHERIT)
                    .start();
            super.br = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));

            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            String message = info.command() + " 실행 실패";
            log.error(message);
            throw new RuntimeException(message);
        }
    }
}
