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
        super(info.command(), sudoPassword);
    }
}
