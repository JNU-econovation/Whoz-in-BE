package com.whoz_in.log_writer.managed;

//Managed 프로세스를 생성하고 처리하는 과정에서 필요한 정보를 담는다.
public record ManagedInfo(String interfaceName, String ssid, String command) {}
