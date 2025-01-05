package com.whoz_in.main_api.shared.utils;

import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TOKEN_KEY_DELIMITER;
import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TOKEN_KEY_EXPIRATION_MIN;

import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//TODO: 패키지 변경 - OAutheTempToken과 같이 담아서 따로 관리

// OAuth 토큰에 담을 소셜 ID 값의 Key를 저장하는 스토어
// Key : OAuth2TokenKey 의 "{hashedKey}::{expiredTime}"
// Value : OAuth2UserInfo 객체
@Component
public class OAuth2UserInfoStore {

    // TODO: Key를 String으로 하지 말고, OAuth2UserInfoKey 객체 그대로 사용하기
    private static final Map<String, OAuth2UserInfo> store = new HashMap<>();

    public OAuth2UserInfoStore(){}

    public static String save(OAuth2UserInfo userInfo){
        String key = OAuth2UserInfoKey.create(userInfo).toString();
        store.put(key, userInfo);
        return key;
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String hashedKey){
        validate(hashedKey);
        if(!store.containsKey(hashedKey)) throw new IllegalArgumentException("소셜 ID-Key 를 찾을 수 없음");

        return store.get(hashedKey);
    }

    private static void validate(String hashedKey){
        try {
            OAuth2UserInfoKey.ensureCorrectKey(hashedKey);
            OAuth2UserInfoKey.ensureNotExpired(hashedKey);
        } catch (IllegalArgumentException e){
            store.remove(hashedKey);
            throw e;
        }
    }

    // hashedKey 와 expiredTime 값을 기준으로 키를 구분한다.
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    public static class OAuth2UserInfoKey {

        @Getter
        @EqualsAndHashCode.Include
        private final String hashedKey;

        @EqualsAndHashCode.Include
        private final long expiredTime;

        public static void ensureNotExpired(String hashedKey){
            long expiredTime = Long.parseLong(hashedKey.split(OAUTH2_TOKEN_KEY_DELIMITER)[1]);

            if (Instant.now().getEpochSecond() > expiredTime)
                throw new IllegalArgumentException("만료된 Social Id Key");
        }

        public static void ensureCorrectKey(String unknownKey){
            if(!unknownKey.contains(OAUTH2_TOKEN_KEY_DELIMITER)
                    || unknownKey.split(OAUTH2_TOKEN_KEY_DELIMITER).length != 2)
                throw new IllegalArgumentException("유효하지 않은 Key 형식");
        }

        private OAuth2UserInfoKey(OAuth2UserInfo userInfo) {
            // TODO: 이 random 을 뭘로 사용해야 할까?
            this.expiredTime = Instant.now()
                    .plus(Duration.ofMinutes(OAUTH2_TOKEN_KEY_EXPIRATION_MIN))
                    .getEpochSecond();
            this.hashedKey = hashing(userInfo, expiredTime);
        }

        public static OAuth2UserInfoKey create(OAuth2UserInfo userInfo) {
            return new OAuth2UserInfoKey(userInfo);
        }

        @Override
        public String toString() {
            String formatString = "%s"+ OAUTH2_TOKEN_KEY_DELIMITER +"%d";
            return String.format(formatString, hashedKey, expiredTime);
        }

        /**
         *
         * @param userInfo
         * @param expiredTime
         * @return hashing(socialId + expiredTime)
         */
        private String hashing(OAuth2UserInfo userInfo, long expiredTime){
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest((userInfo.toString()+expiredTime).getBytes());

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
