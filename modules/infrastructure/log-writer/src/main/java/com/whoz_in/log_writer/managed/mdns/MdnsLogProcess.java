package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.common.process.ContinuousProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;
import lombok.Getter;

@Getter
public class MdnsLogProcess extends ContinuousProcess {
    private final ManagedInfo info;
    public MdnsLogProcess(ManagedInfo info, String sudoPassword) {
        super(info.command(), sudoPassword);
        this.info = info;
    }
}
