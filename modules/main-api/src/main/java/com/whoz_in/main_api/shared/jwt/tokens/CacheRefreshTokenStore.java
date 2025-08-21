package com.whoz_in.main_api.shared.jwt.tokens;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class CacheRefreshTokenStore implements RefreshTokenStore {
    private final Cache<String, Instant> store = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, Instant>() {
                    @Override
                    public long expireAfterCreate(String key, Instant value, long currentTimeNanos) {
                        long remainingMillis = Duration.between(Instant.now(), value).toMillis();
                        return Math.max(TimeUnit.MILLISECONDS.toNanos(remainingMillis), 0);
                    }
                    @Override
                    public long expireAfterUpdate(String key, Instant value, long currentTimeNanos, long currentDurationNanos) {
                        return currentDurationNanos;
                    }
                    @Override
                    public long expireAfterRead(String key, Instant value, long currentTimeNanos, long currentDurationNanos) {
                        return currentDurationNanos;
                    }
            }).build();


    @Override
    public boolean saveIfAbsent(RefreshToken refreshToken) {
        return store.asMap().putIfAbsent(
                refreshToken.getTokenId().toString(),
                refreshToken.getExpiredAt()
        ) == null;
    }
}
