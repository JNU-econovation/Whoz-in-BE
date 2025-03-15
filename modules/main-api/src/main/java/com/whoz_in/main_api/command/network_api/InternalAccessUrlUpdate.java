package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.Command;
import jakarta.validation.constraints.NotNull;

public record InternalAccessUrlUpdate(
        @NotNull String room,
        @NotNull String url
) implements Command {}
