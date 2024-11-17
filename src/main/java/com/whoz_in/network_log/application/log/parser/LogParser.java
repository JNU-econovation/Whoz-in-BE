package com.whoz_in.network_log.application.log.parser;

import java.util.Map;

public interface LogParser {

    Map<String, String> parse(String log);

}
