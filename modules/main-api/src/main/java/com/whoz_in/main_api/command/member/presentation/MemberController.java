package com.whoz_in.main_api.command.member.presentation;

import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.command.member.application.command.LogOut;
import com.whoz_in.main_api.command.member.application.command.LoginSuccessTokens;
import com.whoz_in.main_api.command.member.application.command.MemberOAuth2Login;
import com.whoz_in.main_api.command.member.application.command.MemberOAuth2SignUp;
import com.whoz_in.main_api.command.member.application.command.MemberSignUp;
import com.whoz_in.main_api.command.member.application.command.Reissue;
import com.whoz_in.main_api.command.member.presentation.docs.MemberCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfoStore;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenException;
import com.whoz_in.main_api.shared.jwt.tokens.TokenType;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import com.whoz_in.main_api.shared.utils.CookieFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController extends CommandController implements MemberCommandApi {
  private final TokenSerializer<OAuth2TempToken> oAuth2TempTokenTokenSerializer;
  private final TokenSerializer<AccessToken> accessTokenTokenSerializer;
  private final TokenSerializer<RefreshToken> refreshTokenTokenSerializer;
  private final CookieFactory cookieFactory;
  private final JwtProperties jwtProperties;
  private final OAuth2UserInfoStore oAuth2UserInfoStore;

  public MemberController(CommandBus commandBus,
          TokenSerializer<OAuth2TempToken> oAuth2TempTokenTokenSerializer,
          TokenSerializer<AccessToken> accessTokenTokenSerializer,
          TokenSerializer<RefreshToken> refreshTokenTokenSerializer,
          CookieFactory cookieFactory, JwtProperties jwtProperties,
          OAuth2UserInfoStore oAuth2UserInfoStore) {
    super(commandBus);
    this.oAuth2TempTokenTokenSerializer = oAuth2TempTokenTokenSerializer;
    this.accessTokenTokenSerializer = accessTokenTokenSerializer;
    this.refreshTokenTokenSerializer = refreshTokenTokenSerializer;
    this.cookieFactory = cookieFactory;
    this.jwtProperties = jwtProperties;
    this.oAuth2UserInfoStore = oAuth2UserInfoStore;
  }

  @Override
  @PostMapping("/api/v1/signup")
  public ResponseEntity<SuccessBody<Void>> signup(@RequestBody MemberSignUp request){
    dispatch(request);
    return ResponseEntityGenerator.success( "회원가입 완료", HttpStatus.CREATED);
  }

  @Override
  @PostMapping("/api/v1/signup/oauth")
  public ResponseEntity<SuccessBody<Void>> oAuthSignUp(
          @CookieValue(name = OAUTH2_TEMP_TOKEN) Cookie oAuth2TempTokenCookie,
          @RequestBody MemberOAuthSignUpAdditionalInfo req,
          HttpServletResponse response
  ){
    //사용자의 소셜 정보 가져오기
    OAuth2TempToken token = oAuth2TempTokenTokenSerializer.deserialize(oAuth2TempTokenCookie.getValue())
            .orElseThrow(() -> new TokenException("2002", "잘못된 oauth2 temp token"));
    OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoStore.takeout(token.getUserInfoKey());
    //회원가입
    dispatch(new MemberOAuth2SignUp(oAuth2UserInfo.getSocialProvider(), oAuth2UserInfo.getSocialId(), req.name(), req.position(), req.generation()));
    //자동 로그인
    LoginSuccessTokens tokens = dispatch(new MemberOAuth2Login(oAuth2UserInfo.getSocialId()));
    addTokenCookies(response, tokens);
    return ResponseEntityGenerator.success( "소셜 회원가입 완료", HttpStatus.CREATED);
  }

  @Override
  @PostMapping("/api/v1/reissue")
  public ResponseEntity<SuccessBody<Void>> reissue(
          @CookieValue(name = ACCESS_TOKEN) Cookie atCookie,
          @CookieValue(name = REFRESH_TOKEN) Cookie rtCookie,
          HttpServletResponse response
  ){
    RefreshToken refreshToken = refreshTokenTokenSerializer.deserialize(rtCookie.getValue())
            .orElseThrow(()-> new TokenException("2003", "잘못된 refresh token"));

    removeTokenCookies(response);

    // TODO: 다른 타입을 쓸까?
    LoginSuccessTokens newTokens = dispatch(new Reissue(refreshToken.getMemberId().toString(), refreshToken.getTokenId().toString()));

    addTokenCookies(response, newTokens);
    return ResponseEntityGenerator.success("토큰 재발급 완료", HttpStatus.CREATED);

  }

  @Override
  @PostMapping("/api/v1/logout")
  public ResponseEntity<SuccessBody<Void>> logout(
          @CookieValue(name = ACCESS_TOKEN) Cookie atCookie,
          @CookieValue(name = REFRESH_TOKEN) Cookie rtCookie,
          HttpServletResponse response) {
    RefreshToken refreshToken = refreshTokenTokenSerializer.deserialize(rtCookie.getValue())
                    .orElseThrow(()-> new TokenException("2004", "잘못된 refresh token"));
    AccessToken accessToken = accessTokenTokenSerializer.deserialize(atCookie.getValue())
                    .orElseThrow(() -> new TokenException("2003", "잘못된 access token"));

    LogOut command = new LogOut(
            accessToken.getMemberId().toString(),
            refreshToken.getTokenId().toString());

    dispatch(command);

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
    response.addCookie(accessTokenCookie);
    response.addCookie(refreshTokenCookie);
  }
}
