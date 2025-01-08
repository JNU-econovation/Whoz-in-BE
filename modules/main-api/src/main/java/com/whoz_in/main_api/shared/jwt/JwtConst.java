package com.whoz_in.main_api.shared.jwt;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class JwtConst {

    // token
    public static final String ISSUER = "whozin";
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String OAUTH2_TEMP_TOKEN = "oauth2-temp-token";
    public static final String TOKEN_TYPE = "token-type";
    public static final String TOKEN_ID = "token-id";
    public static final String OAUTH_USER_INFO_KEY = "oauth-user-info-key";

    // auth
    public static final String AUTHORIZATION = "Authorization";
    public static final String MEMBER_ID = "member-id";
    public static final String ACCOUNT_TYPE = "account-type";
    public static final String PERSONAL_AUTHORITY = "personal";

}
