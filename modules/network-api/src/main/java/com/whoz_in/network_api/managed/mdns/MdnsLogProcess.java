package com.whoz_in.network_api.managed.mdns;

import com.whoz_in.network_api.common.process.ContinuousProcess;
import lombok.Getter;

@Getter
public class MdnsLogProcess extends ContinuousProcess {
    public MdnsLogProcess(String command) {
        super(command);
    }
}
