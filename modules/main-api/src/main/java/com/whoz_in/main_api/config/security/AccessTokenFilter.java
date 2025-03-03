package com.whoz_in.main_api.config.security;


import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;

import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.AccessTokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//요청에서 jwt(access token)를 꺼내어 인증정보를 만들고 SecurityContextHolder에 넣는 필터
@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
    private final AccessTokenSerializer accessTokenSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        extractAccessToken(request)
                .map(this::createAuthentication)
                .ifPresentOrElse(this::setAuthentication, SecurityContextHolder::clearContext);
        filterChain.doFilter(request, response);
    }

    private Optional<AccessToken> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .flatMap(accessTokenSerializer::deserialize);
    }

    private JwtAuthentication createAuthentication(AccessToken accessToken){
        return new JwtAuthentication(
                accessToken.getMemberId(),
                Collections.singletonList(new SimpleGrantedAuthority(accessToken.getAccountType().name()))
        ); //TODO: ROLE
    }
    private void setAuthentication(JwtAuthentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
