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
        super(info.command(), sudoPassword);
    }
}
