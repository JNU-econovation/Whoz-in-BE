package com.whoz_in.main_api.shared.jwt.tokens;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class StubTokenStore implements TokenStore {

    private final Cache<String, LocalDateTime> store;
    private final JwtProperties jwtProperties;

    public StubTokenStore(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        this.store = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(jwtProperties.getTokenExpiry(TokenType.REFRESH))
                .build();
    }

    @Override
    public void save(String tokenId) {
        store.put(tokenId, LocalDateTime.now());
    }

    @Override
    public boolean isExist(String tokenId) {
        return store.asMap().containsKey(tokenId);

    }

    @Override
    public void clear() {
        store.invalidateAll();

    }

    @Override
    public void delete(String tokenId) {
        store.invalidate(tokenId);
    }
}
