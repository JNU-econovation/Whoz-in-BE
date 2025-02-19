package com.whoz_in.network_api.managed.arp;

import com.whoz_in.network_api.common.process.TransientProcess;
import lombok.Getter;

@Getter
public class ArpLogProcess extends TransientProcess {
    public ArpLogProcess(String command) {
        super(command);
    }

}
