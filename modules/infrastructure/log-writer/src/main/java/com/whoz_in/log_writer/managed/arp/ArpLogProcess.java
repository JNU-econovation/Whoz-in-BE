package com.whoz_in.log_writer.managed.arp;

import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.IOException;
import lombok.Getter;

@Getter
public class ArpLogProcess extends TransientProcess {
    private final ManagedInfo info;
    public ArpLogProcess(ManagedInfo info, String password) {
        super(info.command(), password);
        this.info = info;
    }

}
