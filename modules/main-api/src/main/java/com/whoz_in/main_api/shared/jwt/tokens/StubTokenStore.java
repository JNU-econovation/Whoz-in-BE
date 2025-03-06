package com.whoz_in.main_api.shared.jwt.tokens;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StubTokenStore implements TokenStore {

    private final Map<String, String> store;

    public StubTokenStore(){
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public void save(String tokenId) {
        store.put(tokenId, System.currentTimeMillis()+3600000+"");
    }

    @Override
    public boolean isExist(String tokenId) {
        return store.containsKey(tokenId);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public void delete(String tokenId) {
        store.remove(tokenId);
    }
}
