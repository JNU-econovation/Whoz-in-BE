package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.common.process.ContinuousProcess;
import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;

public class MdnsLogProcess extends ContinuousProcess {

    public MdnsLogProcess(ManagedInfo info, String sudoPassword) {
        try {
            super.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(Redirect.INHERIT)
                    .start();
            super.br = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(this.process.getInputStream())));
            Writer writer = new OutputStreamWriter(this.process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {

            throw new RuntimeException(info.command() + " 실행 실패");
        }
    }
}
