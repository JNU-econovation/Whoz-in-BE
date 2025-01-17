package com.whoz_in.network_api.private_ip;

import java.util.Map;

//main-api에게 자신(network-api)이 가진 내부 아이피를 알리는 기능
public interface PrivateIpWriter {
    void write(String room, Map<String, String> privateIps); //Map<방, Optional<아이피>>
}
