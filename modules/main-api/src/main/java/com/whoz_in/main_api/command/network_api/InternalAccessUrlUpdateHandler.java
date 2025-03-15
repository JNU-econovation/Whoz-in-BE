package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.security.DynamicCorsConfigurationSource;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.caching.network_api.InternalAccessUrlStore;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class InternalAccessUrlUpdateHandler implements CommandHandler<InternalAccessUrlUpdate, Void> {
    private final DynamicCorsConfigurationSource corsConfig;
    private final InternalAccessUrlStore internalAccessUrlStore;

    @Override
    public Void handle(InternalAccessUrlUpdate command) {
        // cors origins
        if (!corsConfig.isAllowedOrigin(command.url()))
            corsConfig.addAllowedOrigin(command.url());
        internalAccessUrlStore.put(command.room(), command.url());
        return null;
    }
}
