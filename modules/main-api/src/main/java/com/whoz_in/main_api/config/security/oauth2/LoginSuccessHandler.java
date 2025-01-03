package com.whoz_in.main_api.config.security.oauth2;

import static com.whoz_in.main_api.config.security.consts.JwtConst.IS_REGISTERED;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_LOGIN_TOKEN;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_TEMP_TOKEN;

import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginTokenProvider;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempTokenSerializer;
import com.whoz_in.main_api.shared.utils.CookieFactory;
import com.whoz_in.main_api.shared.utils.OAuth2UserInfoStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Qualifier("basic")
    private final CookieFactory cookieFactory;
    private final UriBuilderFactory uriBuilderFactory;
    private final OAuth2LoginTokenProvider oAuth2LoginTokenProvider;
    private final OAuth2TempTokenSerializer oaUth2TempTokenSerializer;

    // registered = true 일 경우, OAuth2LoginToken 을 직렬화 한 jwt 토큰 전송
    // registered = false 일 경우, 추가적인 사용자 정보를 입력받아야 하므로, 임시 jwt 토큰 전송

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2UserInfo userInfo) {

            if(userInfo.isRegistered()) {
                addOAuth2AccessTokenCookie(response,
                        new OAuth2LoginToken(userInfo.getSocialProvider(), userInfo.getSocialId(), userInfo.getName()));
            } else {
                String userInfoKey = OAuth2UserInfoStore.save(userInfo);
                addOAuth2TempTokenCookie(response, new OAuth2TempToken(userInfoKey));
            }

            String uri = uriBuilderFactory.uriString("/oauth/success")//TODO: Static
                    .queryParam(IS_REGISTERED, userInfo.isRegistered())
                    .build()
                    .toString();
            response.sendRedirect(uri);
            return;
        }
        throw new IllegalStateException("이러면 안되는데?");
    }

    private void addOAuth2AccessTokenCookie(HttpServletResponse response, OAuth2LoginToken oAuth2LoginToken) {
        Cookie oAuth2LoginInfoTokenCookie = cookieFactory.create(
                OAUTH2_LOGIN_TOKEN,
                oAuth2LoginTokenProvider.serialize(oAuth2LoginToken)
        );
        response.addCookie(oAuth2LoginInfoTokenCookie);
    }

    private void addOAuth2TempTokenCookie(HttpServletResponse response, OAuth2TempToken oAuth2TempToken) {
        Cookie oAuth2TempTokenCookie = cookieFactory.create(
                OAUTH2_TEMP_TOKEN,
                oaUth2TempTokenSerializer.serialize(oAuth2TempToken)
        );
        response.addCookie(oAuth2TempTokenCookie);
    }

}
