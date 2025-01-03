package com.whoz_in.main_api.config.security.oauth2;

import static com.whoz_in.main_api.config.security.consts.JwtConst.IS_REGISTERED;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_LOGIN_TOKEN;

import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginTokenProvider;
import com.whoz_in.main_api.shared.utils.CookieFactory;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2UserInfo userInfo) {
            Cookie oAuth2LoginInfoTokenCookie = cookieFactory.create(
                    OAUTH2_LOGIN_TOKEN,
                    oAuth2LoginTokenProvider.serialize(new OAuth2LoginToken(userInfo.getSocialProvider(), userInfo.getSocialId(), userInfo.getName()))
            );
            response.addCookie(oAuth2LoginInfoTokenCookie);

            String uri = uriBuilderFactory.uriString("/oauth/success")//TODO: Static
                    .queryParam(IS_REGISTERED, userInfo.isRegistered())
                    .build()
                    .toString();
            response.sendRedirect(uri);
            return;
        }
        throw new IllegalStateException("이러면 안되는데?");
    }
}
