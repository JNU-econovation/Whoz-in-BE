package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.security.DynamicCorsConfigurationSource;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class CorsOriginUpdateHandler implements CommandHandler<CorsOriginUpdate, Void> {
    private final DynamicCorsConfigurationSource corsConfig;
    @Override
    public Void handle(CorsOriginUpdate command) {
        if (!corsConfig.isAllowedOrigin(command.origin()))
            corsConfig.addAllowedOrigin(command.origin());
        return null;
    }
}
