package com.whoz_in.main_api.shared.caching.network_api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalAccessUrlStore {
    private static final Cache<String, String> store = CacheBuilder.newBuilder() // Cache<Room, url>
            .expireAfterAccess(1, TimeUnit.MINUTES) // 1분 동안 접근이 없으면 해당 url을 신뢰하지 않고 삭제
            .build();

    public void put(String room, String url) {
        store.put(room, url);
    }

    public Optional<String> get(String room){
        return Optional.ofNullable(store.getIfPresent(room));
    }
}
