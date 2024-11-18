package com.whoz_in.network_log.domain.managed.parser;

import com.whoz_in.network_log.domain.managed.LogDTO;
import java.util.Map;

public interface LogParser {

    LogDTO parse(String log);

}
