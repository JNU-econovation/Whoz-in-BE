package com.whoz_in.main_api.shared.jwt;

import com.whoz_in.main_api.shared.jwt.tokens.StubTokenStore;
import com.whoz_in.main_api.shared.jwt.tokens.TokenStore;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TokenStoreTest {

    private TokenStore tokenStore;
    private Duration testDuration = Duration.ofSeconds(30);

    public TokenStoreTest(){
        JwtProperties jwtProperties = new JwtProperties("secret", testDuration, testDuration, testDuration, testDuration);
        this.tokenStore = new StubTokenStore(jwtProperties);
    }

    @Test
    @DisplayName("이미_저장된_토큰은_true를_반환한다")
    void isExistTest(){
        String tokenId = UUID.randomUUID().toString();

        tokenStore.save(tokenId);
        boolean isExist = tokenStore.isExist(tokenId);

        Assertions.assertTrue(isExist);
    }

    @Test
    @DisplayName("삭제한_토큰은_false를_반환한다")
    void isExistTest2(){
        String tokenId = UUID.randomUUID().toString();

        tokenStore.save(tokenId);
        tokenStore.delete(tokenId);

        boolean isExist = tokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);
    }

    @Test
    @DisplayName("저장소를_초기화하면_false를_반환한다")
    void isExistTest3(){
        String tokenId = UUID.randomUUID().toString();

        tokenStore.save(tokenId);
        tokenStore.clear();

        boolean isExist = tokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);
    }

    @Test
    @DisplayName("저장하지_않은_토큰은_false를_반환한다")
    void isExistTest4(){
        String tokenId = UUID.randomUUID().toString();

        // tokenStore.save(tokenId);

        boolean isExist = tokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);

    }

}
