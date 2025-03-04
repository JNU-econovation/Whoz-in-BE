package com.whoz_in.main_api.command.member.presentation.docs;

import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;

import com.whoz_in.main_api.command.member.application.MemberSignUp;
import com.whoz_in.main_api.command.member.presentation.MemberOAuthSignUpAdditionalInfo;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원", description = "회원 Command Api")
public interface MemberCommandApi {

    @Operation(
            summary = "회원가입",
            description = "ID/PW로 회원가입을 진행합니다."
    )
    ResponseEntity<SuccessBody<Void>> signup(@RequestBody MemberSignUp request);


    @Operation(
            summary = "소셜 회원가입",
            description = "사용자의 추가 정보를 받고 소셜 회원가입을 진행합니다."
    )
    public ResponseEntity<SuccessBody<Void>> oAuthSignUp(
            @CookieValue(name = OAUTH2_TEMP_TOKEN) Cookie oAuth2TempTokenCookie,
            @RequestBody MemberOAuthSignUpAdditionalInfo req,
            HttpServletResponse response
    );

}
