package com.whoz_in.main_api.config.security.oauth2;


import static com.whoz_in.main_api.shared.jwt.JwtConst.AUTHORIZATION;

import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.AccessTokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

//요청에서 jwt를 꺼내어 인증정보를 만들고 SecurityContextHolder에 넣는 필터
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AccessTokenSerializer accessTokenSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        AccessToken accessToken = accessTokenSerializer.deserialize(token.substring(7));

        JwtAuthentication authentication = new JwtAuthentication(
                accessToken.getMemberId(),
                Collections.singletonList(new SimpleGrantedAuthority(accessToken.getAccountType().name()))
        ); //TODO: ROLE
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
