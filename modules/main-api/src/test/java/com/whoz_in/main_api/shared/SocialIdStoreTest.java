package com.whoz_in.main_api.shared;

import com.whoz_in.main_api.shared.utils.SocialIdStore;
import com.whoz_in.main_api.shared.utils.SocialIdStore.SocialIdKey;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SocialIdStoreTest {

    @Test
    @DisplayName("SocialIdKey 생성 테스트")
    void SocialIdKey생성테스트() {
        SocialIdKey key = SocialIdKey.create("12356");
        System.out.println(key);
    }

    @Test
    @DisplayName("정상적인 SocialIdKey 테스트")
    void SocialIdKey만료시간테스트() {
        SocialIdKey key = SocialIdKey.create("12356");
        System.out.println(key);
        System.out.println("현재 시간 : " + Instant.now().getEpochSecond());
        Assertions.assertDoesNotThrow(key::ensureNotExpired);
    }

    @Test
    @DisplayName("만료된 SocialIdKey 테스트")
    void SocialIdKey만료시간테스트2() {
        SocialIdKey key = SocialIdKey.create("12356");
        System.out.println(key);
        System.out.println("4분 잠자기..");
        try {
            Thread.sleep(240000);
        } catch (InterruptedException e){
            Thread.currentThread().start();
        }

        Assertions.assertThrows(IllegalArgumentException.class, key::ensureNotExpired);
    }

    @Test
    @DisplayName("저장소 테스트")
    void SocialIdKey저장소테스트() {
        SocialIdStore store = new SocialIdStore();
        String socialId = "12356";
        SocialIdKey key = store.save(socialId);
        System.out.println("발급된 key : " + key);
        System.out.println("현재 시간 : " + Instant.now().getEpochSecond());
        Assertions.assertDoesNotThrow(key::ensureNotExpired);
    }

}
