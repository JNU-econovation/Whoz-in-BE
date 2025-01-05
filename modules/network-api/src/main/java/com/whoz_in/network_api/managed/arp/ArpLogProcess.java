package com.whoz_in.network_api.managed.arp;

import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.managed.ManagedInfo;
import lombok.Getter;

@Getter
public class ArpLogProcess extends TransientProcess {
    private final ManagedInfo info;
    public ArpLogProcess(ManagedInfo info, String password) {
        super(info.command(), password);
        this.info = info;
    }

}
