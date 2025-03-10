package com.whoz_in.main_api.command.member.presentation.docs;

import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.command.member.application.command.MemberSignUp;
import com.whoz_in.main_api.command.member.presentation.MemberOAuthSignUpAdditionalInfo;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원, 인증, 인가", description = "회원 Command Api")
public interface MemberCommandApi {

    @Operation(
            summary = "회원가입",
            description = "ID/PW로 회원가입을 진행합니다."
    )
    ResponseEntity<SuccessBody<Void>> signup(@RequestBody MemberSignUp request);


    @Operation(
            summary = "소셜 회원가입",
            description = """
                    사용자의 추가 정보를 받고 소셜 회원가입을 진행합니다.
                    
                    아래의 쿠키가 필요합니다.
                    
                    - oauth2-temp-token
                    """
    )
    ResponseEntity<SuccessBody<Void>> oAuthSignUp(
            @Parameter(hidden = true) Cookie oAuth2TempTokenCookie,
            @RequestBody MemberOAuthSignUpAdditionalInfo req,
            HttpServletResponse response
    );

    @Operation(
            summary = "토큰 재발급",
            description = """
                    액세스 토큰과 리프레시 토큰을 이용하여, 토큰을 재발급합니다.
                    
                    아래의 쿠키가 필요합니다.
                    
                    - access-token
                    - refresh-token
                    """
    )
    ResponseEntity<SuccessBody<Void>> reissue(
            @Parameter(hidden = true) Cookie accessTokenCookie,
            @Parameter(hidden = true) Cookie refreshTokenCookie,
            @Parameter(hidden = true) HttpServletResponse response
    );

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 수행합니다."
    )
    ResponseEntity<SuccessBody<Void>> logout(
            @Parameter(hidden = true) Cookie accessTokenCookie,
            @Parameter(hidden = true) Cookie refreshTokenCookie,
            @Parameter(hidden = true) HttpServletResponse response
    );


}
