package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record PrivateIpList(
        List<String> ipList
) implements Response {}
