package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.security.DynamicCorsConfigurationSource;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.application.caching.network_api.RoomAccessUrlStore;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class RoomAccessUrlUpdateHandler implements CommandHandler<RoomAccessUrlUpdate, Void> {
    private final DynamicCorsConfigurationSource corsConfig;
    private final RoomAccessUrlStore roomAccessUrlStore;

    @Override
    public Void handle(RoomAccessUrlUpdate command) {
        // cors origins
        if (!corsConfig.isAllowedOrigin(command.url()))
            corsConfig.addAllowedOrigin(command.url());
        roomAccessUrlStore.put(command.room(), command.url());
        return null;
    }
}
