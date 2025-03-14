package com.whoz_in.network_api.common.network_interface;

import java.util.Map;

public interface NetworkAddressResolver {
    // 연결된 인터페이스들만 반환합니다. (value가 null인 key는 없다는 뜻)
    Map<String, NetworkAddress> resolve();
}
