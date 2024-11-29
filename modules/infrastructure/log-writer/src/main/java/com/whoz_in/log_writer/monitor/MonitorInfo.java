package com.whoz_in.log_writer.monitor;

//TODO: interfaceName 제거
//Monitor 프로세스를 생성하고 처리하는 과정에서 필요한 정보를 담는다.
public record MonitorInfo(String interfaceName, String command) {}