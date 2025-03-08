package com.whoz_in.network_api.common.network_interface;

import com.whoz_in.domain.shared.Nullable;

// NetworkInterface에 알맞는 설정값을 (실행할 command, 어떤 와이파이의 패킷을 잡는지 나타내는 ssid) 가진다.
public record NetworkInterfaceProfile(
        NetworkInterface ni, String command, @Nullable String ssid
) {}
