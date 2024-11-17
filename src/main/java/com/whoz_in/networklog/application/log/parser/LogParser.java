package com.whoz_in.networklog.application.log.parser;

import java.util.Map;

public interface LogParser {

    Map<String, String> parse(String log);

}
