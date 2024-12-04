package com.whoz_in.log_writer.managed.arp;

import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;

public class ArpLogProcess extends TransientProcess {
    public ArpLogProcess(ManagedInfo info, String password) {
        super(info.command(), password);
    }

}
