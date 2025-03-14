package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.Command;

public record CorsOriginUpdate(
        String room,
        String origin
) implements Command {}
