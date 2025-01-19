package com.whoz_in.main_api.config.security.oauth2;

import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.command.member.application.LoginSuccessTokens;
import com.whoz_in.main_api.command.member.application.MemberOAuth2Login;
import com.whoz_in.main_api.command.member.application.MemberOAuth2LoginHandler;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.TokenType;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import com.whoz_in.main_api.shared.utils.CookieFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;

//사용자가 소셜 로그인에 성공했을 경우 처리하는 핸들러
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String ENDPOINT = "/oauth/success"; //로그인 성공 시 이동시킬 엔드포인트임. 프론트가 이 페이지를 구현해야 한다.
    private final CookieFactory cookieFactory;
    private final UriBuilderFactory uriBuilderFactory;
    private final JwtProperties jwtProperties;
    private final TokenSerializer<OAuth2TempToken> oAuth2TempTokenSerializer;
    private final MemberOAuth2LoginHandler handler;
    private final OAuth2UserInfoStore oAuth2UserInfoStore;

    //이미 회원가입한 회원이면 Access/Refresh를 제공
    //아니면 OAuthTempToken을 제공
    //(모두 쿠키로 전달합니다)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        OAuth2UserInfo userInfo = (OAuth2UserInfo) authentication.getPrincipal();

        if(userInfo.isRegistered()) {
            //이미 소셜 회원이 존재할 때
            //소셜 로그인 핸들러를 호출하여 AccessToken과 RefreshToken을 받고 클라이언트로 전송합니다.
            LoginSuccessTokens tokens = handler.handle(new MemberOAuth2Login(userInfo.getSocialId()));
            response.addCookie(cookieFactory.create(ACCESS_TOKEN, tokens.accessToken(), jwtProperties.getTokenExpiry(TokenType.ACCESS)));
            response.addCookie(cookieFactory.create(REFRESH_TOKEN, tokens.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH)));
        } else {
            //소셜 회원이 없을 때
            //소셜 정보를 저장하고, 정보를 찾을 수 있는 키를 반환한다.
            //이후 키를 통해 회원가입이나 소셜 계정 등록에 필요한 소셜 정보를 찾는다.
            String userInfoKey = oAuth2UserInfoStore.save(userInfo);
            Cookie oAuth2TempTokenCookie = cookieFactory.create(
                    OAUTH2_TEMP_TOKEN,
                    oAuth2TempTokenSerializer.serialize(new OAuth2TempToken(userInfoKey))
            );
            response.addCookie(oAuth2TempTokenCookie);
        }
        String frontUrl = request.getHeader("Referer");
        String uri = uriBuilderFactory.uriString( frontUrl + ENDPOINT)
                .queryParam("is-registered", userInfo.isRegistered())
                .build()
                .toString();
        response.sendRedirect(uri);
    }
}
