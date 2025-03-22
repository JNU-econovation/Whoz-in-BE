package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.main_api.shared.utils.RequesterInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;

//사용자가 소셜 로그인에 실패했을 경우 처리하는 핸들러
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${frontend.main.base-url}")
    private String frontendBaseUrl;
    private final UriBuilderFactory uriBuilderFactory;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.warn("소셜 로그인 실패: {}", exception.getMessage());
        String uri = uriBuilderFactory.uriString(frontendBaseUrl)
                .build()
                .toString();
        response.sendRedirect(uri);
    }
}
