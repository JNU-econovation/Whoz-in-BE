package com.whoz_in.main_api.command.member.presentation;

import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;

import com.whoz_in.main_api.command.member.application.MemberOAuth2SignUp;
import com.whoz_in.main_api.command.member.application.MemberSignUp;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController extends CommandController {
  public MemberController(CommandBus commandBus) {
    super(commandBus);
  }

  @PostMapping("/api/v1/signup")
  public ResponseEntity<SuccessBody<Void>> signup(@RequestBody MemberSignUp request){
    dispatch(request);
    return ResponseEntityGenerator.success( "회원가입 완료", HttpStatus.CREATED);
  }

  @PostMapping("/api/v1/signup/oauth")
  public ResponseEntity<SuccessBody<Void>> oAuthSignUp(
          @CookieValue(name = OAUTH2_TEMP_TOKEN) Cookie oAuth2TempTokenCookie,
          @RequestBody MemberOAuthSignUpAdditionalInfo req){

    dispatch(new MemberOAuth2SignUp(oAuth2TempTokenCookie.getValue(), req.name(), req.position(), req.generation()));
    return ResponseEntityGenerator.success( "소셜 회원가입 완료", HttpStatus.CREATED);
  }
}
