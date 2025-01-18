package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.Map;

public record PrivateIps(
        Map<String, String> ipList //Map<SSID, private ip>
) implements Response {}
