package com.whoz_in.main_api.command.member.presentation;

import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.command.member.application.LogOut;
import com.whoz_in.main_api.command.member.application.LoginSuccessTokens;
import com.whoz_in.main_api.command.member.application.MemberOAuth2Login;
import com.whoz_in.main_api.command.member.application.MemberOAuth2SignUp;
import com.whoz_in.main_api.command.member.application.Reissue;
import com.whoz_in.main_api.command.member.presentation.docs.MemberCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfoStore;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenType;
import com.whoz_in.main_api.shared.presentation.cookie.CookieFactory;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController extends CommandController implements MemberCommandApi {
  private final CookieFactory cookieFactory;
  private final JwtProperties jwtProperties;
  private final OAuth2UserInfoStore oAuth2UserInfoStore;

  public MemberController(CommandBus commandBus,
          CookieFactory cookieFactory, JwtProperties jwtProperties,
          OAuth2UserInfoStore oAuth2UserInfoStore) {
    super(commandBus);
    this.cookieFactory = cookieFactory;
    this.jwtProperties = jwtProperties;
    this.oAuth2UserInfoStore = oAuth2UserInfoStore;
  }

  @Override
  @PostMapping("/api/v1/signup/oauth")
  public ResponseEntity<SuccessBody<Void>> oAuthSignUp(
          OAuth2TempToken oAuth2TempToken,
          @RequestBody MemberOAuthSignUpAdditionalInfo req,
          HttpServletResponse response
  ){
    // 한 번 꺼내면 삭제됐기 때문에 api 재호출 불가
    OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoStore.takeout(oAuth2TempToken.getUserInfoKey());
    //회원가입
    dispatch(new MemberOAuth2SignUp(oAuth2UserInfo.getSocialProvider(), oAuth2UserInfo.getSocialId(), req.name(), req.position(), req.generation()));
    //자동 로그인
    LoginSuccessTokens tokens = dispatch(new MemberOAuth2Login(oAuth2UserInfo.getSocialId()));
    addTokenCookies(response, tokens);
    return ResponseEntityGenerator.success( "소셜 회원가입 완료", HttpStatus.CREATED);
  }

  @Override
  @PostMapping("/api/v1/reissue")
  public ResponseEntity<SuccessBody<Void>> reissue(RefreshToken refreshToken, HttpServletResponse response){
    removeTokenCookies(response);

    LoginSuccessTokens newTokens = dispatch(new Reissue(refreshToken));

    addTokenCookies(response, newTokens);
    return ResponseEntityGenerator.success("토큰 재발급 완료", HttpStatus.CREATED);
  }

  @Override
  @PostMapping("/api/v1/logout")
  public ResponseEntity<SuccessBody<Void>> logout(
          AccessToken accessToken,
          RefreshToken refreshToken,
          HttpServletResponse response) {

    dispatch(new LogOut(refreshToken));
    removeTokenCookies(response);

    return ResponseEntityGenerator.success("로그아웃 성공", HttpStatus.OK);
  }

  private void removeTokenCookies(HttpServletResponse response) {
    Cookie emptyAccess = cookieFactory.createDeletionCookie(ACCESS_TOKEN);
    Cookie emptyRefresh = cookieFactory.createDeletionCookie(REFRESH_TOKEN);
    response.addCookie(emptyAccess);
    response.addCookie(emptyRefresh);
  }


  private void addTokenCookies(HttpServletResponse response, LoginSuccessTokens tokens){
    Cookie accessTokenCookie = cookieFactory.create(ACCESS_TOKEN, tokens.accessToken(), jwtProperties.getTokenExpiry(TokenType.ACCESS));
    Cookie refreshTokenCookie = cookieFactory.create(REFRESH_TOKEN, tokens.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH));
    refreshTokenCookie.setPath("/api/v1/reissue");
    response.addCookie(accessTokenCookie);
    response.addCookie(refreshTokenCookie);
  }
}
