package com.whoz_in.main_api.shared.utils;

import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_TOKEN_KEY_DELIMITER;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_TOKEN_KEY_EXPIRATION_MIN;

import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginToken;
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
public class OAuth2TokenStore {

    private static final Map<OAuth2TokenKey, OAuth2LoginToken> store = new HashMap<>();

    public OAuth2TokenStore(){}

    public static OAuth2TokenKey save(OAuth2LoginToken token){
        OAuth2TokenKey key = OAuth2TokenKey.create(token);
        store.put(key, token);
        return key;
    }

    public static OAuth2LoginToken getSocialId(OAuth2TokenKey key){
        validate(key);
        if(!store.containsKey(key)) throw new IllegalArgumentException("소셜 ID-Key 를 찾을 수 없음");

        return store.get(key);
    }

    private static void validate(OAuth2TokenKey key){
        try {
            key.ensureNotExpired();
        } catch (IllegalArgumentException e){
            store.remove(key);
            throw e;
        }
    }

    // hashedKey 와 expiredTime 값을 기준으로 키를 구분한다.
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    public static class OAuth2TokenKey {

        @EqualsAndHashCode.Include
        private final String hashedKey;

        @EqualsAndHashCode.Include
        private final long expiredTime;

        private OAuth2TokenKey(OAuth2LoginToken token) {
            // TODO: 이 random 을 뭘로 사용해야 할까?
            this.expiredTime = Instant.now()
                    .plus(Duration.ofMinutes(OAUTH2_TOKEN_KEY_EXPIRATION_MIN))
                    .getEpochSecond();
            this.hashedKey = hashing(token, expiredTime);
        }

        public void ensureNotExpired(){
            if (Instant.now().getEpochSecond() > this.expiredTime)
                throw new IllegalArgumentException("만료된 Social Id Key");
        }

        public static OAuth2TokenKey create(OAuth2LoginToken token) {
            return new OAuth2TokenKey(token);
        }

        @Override
        public String toString() {
            String formatString = "%s"+ OAUTH2_TOKEN_KEY_DELIMITER +"%d";
            return String.format(formatString, hashedKey, expiredTime);
        }

        /**
         *
         * @param token
         * @param expiredTime
         * @return hashing(socialId + expiredTime)
         */
        private String hashing(OAuth2LoginToken token, long expiredTime){
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest((token.toString()+expiredTime).getBytes());

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
