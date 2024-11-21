package com.whoz_in.network_log.infra.managed.parser;

import com.whoz_in.network_log.infra.managed.LogDTO;

public interface LogParser {

    LogDTO parse(String log);

}
