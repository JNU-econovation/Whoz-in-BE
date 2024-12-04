package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.common.process.ContinuousProcess;
import com.whoz_in.log_writer.managed.ManagedInfo;

public class MdnsLogProcess extends ContinuousProcess {

    public MdnsLogProcess(ManagedInfo info, String sudoPassword) {
        super(info.command(), sudoPassword);
    }
}
