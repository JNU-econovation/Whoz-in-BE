package com.whoz_in.network_log.infra.managed.mdns.parser;

import com.whoz_in.network_log.infra.managed.mdns.LogDTO;

public interface LogParser {

    LogDTO parse(String log);

}
