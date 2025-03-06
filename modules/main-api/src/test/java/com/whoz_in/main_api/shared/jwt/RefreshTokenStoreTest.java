package com.whoz_in.main_api.shared.jwt;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.whoz_in.main_api.command.member.application.token.RefreshTokenStore;
import com.whoz_in.main_api.command.member.application.token.StubRefreshTokenStore;
import java.util.UUID;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class RefreshTokenStoreTest {

    @Mock private final RefreshTokenStore refreshTokenStore;

    public RefreshTokenStoreTest() {
        this.refreshTokenStore = new StubRefreshTokenStore();
    }

    @Test
    @DisplayName("이미_저장된_토큰은_true를_반환한다")
    void isExistTest(){
        String tokenId = UUID.randomUUID().toString();

        refreshTokenStore.save(tokenId);

        boolean isExist = refreshTokenStore.isExist(tokenId);

        Assertions.assertTrue(isExist);
    }

    @Test
    @DisplayName("삭제한_토큰은_false를_반환한다")
    void isExistTest2(){
        String tokenId = UUID.randomUUID().toString();

        refreshTokenStore.save(tokenId);
        refreshTokenStore.delete(tokenId);

        boolean isExist = refreshTokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);
    }

    @Test
    @DisplayName("저장소를_초기화하면_false를_반환한다")
    void isExistTest3(){
        String tokenId = UUID.randomUUID().toString();

        refreshTokenStore.save(tokenId);
        refreshTokenStore.clear();

        boolean isExist = refreshTokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);
    }

    @Test
    @DisplayName("저장하지_않은_토큰은_false를_반환한다")
    void isExistTest4(){
        String tokenId = UUID.randomUUID().toString();

        // refreshTokenStore.save(tokenId);

        boolean isExist = refreshTokenStore.isExist(tokenId);

        Assertions.assertFalse(isExist);

    }

}
