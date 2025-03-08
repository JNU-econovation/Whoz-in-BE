package com.whoz_in.network_api.common.network_interface;

import java.util.Map;

// 무선 인터페이스가 존재할 경우 상태가 어떻든 Map에 넣어야 함
public interface WirelessInfoResolver {
    Map<String, WirelessInfo> resolve();
}
