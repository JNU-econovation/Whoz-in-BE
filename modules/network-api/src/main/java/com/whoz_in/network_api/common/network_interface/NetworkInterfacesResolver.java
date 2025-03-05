package com.whoz_in.network_api.common.network_interface;

import java.util.List;

//실시간으로 시스템에 존재하는 네트워크 인터페이스들을 얻을 수 있는 기능을 제공해야 한다.
public interface NetworkInterfacesResolver {
    List<NetworkInterface> getLatest();
    default boolean exists(NetworkInterface ni){
        return getLatest().contains(ni);
    }
}
