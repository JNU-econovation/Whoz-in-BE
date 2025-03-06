package com.whoz_in.main_api.command.member.application.token;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StubRefreshTokenStore implements RefreshTokenStore{

    private final Map<String, String> store;

    public StubRefreshTokenStore(){
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public void save(String refreshTokenId) {
        store.put(refreshTokenId, System.currentTimeMillis()+3600000+"");
    }

    @Override
    public boolean isExist(String refreshTokenId) {
        return store.containsKey(refreshTokenId);
    }
}
