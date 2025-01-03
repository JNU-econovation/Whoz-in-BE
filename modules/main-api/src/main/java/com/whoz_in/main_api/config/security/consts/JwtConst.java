package com.whoz_in.main_api.config.security.consts;

public class JwtConst {

    // token
    public static final String ISSUER = "whozin";
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String OAUTH2_LOGIN_TOKEN = "oauth2-login-token";
    public static final String OAUTH2_TEMP_TOKEN = "oauth2-temp-token";
    public static final String TOKEN_TYPE = "token-type";
    public static final String TOKEN_ID = "token-id";
    public static final String USER_INFO_KEY = "oauth-user-info-key";
    public static final String OAUTH2_TOKEN_KEY_DELIMITER = "::";
    public static final long OAUTH2_TOKEN_KEY_EXPIRATION_MIN = 3;

    // auth
    public static final String AUTHORIZATION = "Authorization";
    public static final String SOCIAL_PROVIDER = "social-provider";
    public static final String SOCIAL_ID = "social-id";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String IS_REGISTERED = "is-registered";
    public static final String USER_ID = "user-id";
    public static final String ACCOUNT_TYPE = "account-type";
    public static final String PERSONAL_AUTHORITY = "personal";

}
