package com.whoz_in.log_writer.managed.arp;

import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;

public class ArpLogProcess extends TransientProcess {
    public ArpLogProcess(ManagedInfo info, String password) {
        try {
            //TODO: error 처리 로직 수정
            super.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(Redirect.INHERIT)
                    .start();
            super.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bw.write(password);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
