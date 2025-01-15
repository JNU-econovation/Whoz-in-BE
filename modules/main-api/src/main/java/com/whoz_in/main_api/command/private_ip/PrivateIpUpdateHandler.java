package com.whoz_in.main_api.command.private_ip;

import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.caching.private_ip.PrivateIpStore;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class PrivateIpUpdateHandler implements CommandHandler<PrivateIpUpdate, Void> {
    private final PrivateIpStore privateIpStore;

    @Override
    public Void handle(PrivateIpUpdate command) {
        privateIpStore.put(command.room(), command.privateIpList());
        return null;
    }
}
