package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.caching.private_ip.PrivateIpStore;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class PrivateIpsGetHandler implements QueryHandler<PrivateIpsGet, PrivateIps> {
    private final PrivateIpStore privateIpStore;
    @Override
    public PrivateIps handle(PrivateIpsGet query) {
        return new PrivateIps(
                (query.room() != null) ?
                        privateIpStore.get(query.room()) :
                        privateIpStore.get()
        );
    }
}
