package com.whoz_in.main_api.shared.utils;

import static com.whoz_in.main_api.config.security.consts.JwtConst.SOCIAL_ID_KEY_DELIMITER;
import static com.whoz_in.main_api.config.security.consts.JwtConst.SOCIAL_ID_KEY_EXPIRATION_MIN;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// OAuth 토큰에 담을 소셜 ID 값의 Key를 저장하는 스토어
@Component
@RequiredArgsConstructor
public class SocialIdStore {

    private final Map<SocialIdKey, String> store;

    public SocialIdStore() {
        this.store = new HashMap<>();
    }

    public SocialIdKey save(String socialId){
        SocialIdKey key = SocialIdKey.create(socialId);
        this.store.put(key, socialId);
        return key;
    }

    public String getSocialId(SocialIdKey key){
        validate(key);
        if(!this.store.containsKey(key)) throw new IllegalArgumentException("소셜 ID-Key 를 찾을 수 없음");

        return store.get(key);
    }

    private void validate(SocialIdKey key){
        try {
            key.ensureNotExpired();
        } catch (IllegalArgumentException e){
            this.store.remove(key);
            throw e;
        }
    }

    // hashedKey 와 expiredTime 값을 기준으로 키를 구분한다.
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    public static class SocialIdKey {

        @EqualsAndHashCode.Include
        private final String hashedKey;

        @EqualsAndHashCode.Include
        private final long expiredTime;

        private SocialIdKey(String socialId) {
            // TODO: 이 random 을 뭘로 사용해야 할까?
            this.expiredTime = Instant.now()
                    .plus(Duration.ofMinutes(SOCIAL_ID_KEY_EXPIRATION_MIN))
                    .getEpochSecond();
            this.hashedKey = hashing(socialId, expiredTime);
        }

        public void ensureNotExpired(){
            if (Instant.now().getEpochSecond() > this.expiredTime)
                throw new IllegalArgumentException("만료된 Social Id Key");
        }

        public static SocialIdKey create(String socialId) {
            return new SocialIdKey(socialId);
        }

        @Override
        public String toString() {
            String formatString = "%s"+SOCIAL_ID_KEY_DELIMITER+"%d";
            return String.format(formatString, hashedKey, expiredTime);
        }

        /**
         *
         * @param socialId
         * @param expiredTime
         * @return hashing(socialId + expiredTime)
         */
        private String hashing(String socialId, long expiredTime){
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest((socialId+expiredTime).getBytes());

                StringBuilder hexString = new StringBuilder();
                for(byte b : hash){
                    String hex = Integer.toHexString(0xff & b);
                    if(hex.length()==1) hexString.append(hex);
                    hexString.append(hex);
                }

                return hexString.toString();

            } catch (NoSuchAlgorithmException e){
                throw new IllegalArgumentException("존재하지 않는 알고리즘");
            }

        }

    }



}
