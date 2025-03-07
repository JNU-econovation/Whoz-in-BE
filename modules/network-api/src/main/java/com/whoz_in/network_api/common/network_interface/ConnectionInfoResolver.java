package com.whoz_in.network_api.common.network_interface;

import java.util.Map;

public interface ConnectionInfoResolver {
    Map<String, ConnectionInfo> resolve();
}
