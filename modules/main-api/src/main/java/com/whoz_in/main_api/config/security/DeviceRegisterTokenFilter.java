package com.whoz_in.main_api.config.security;


import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//요청에서 jwt를 꺼내어 인증정보를 만들고 SecurityContextHolder에 넣는 필터
@Component
@RequiredArgsConstructor
public class DeviceRegisterTokenFilter extends OncePerRequestFilter {
    private final TokenSerializer<DeviceRegisterToken> deviceRegisterTokenSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        extractDeviceRegisterToken(request)
                .map(this::createAuthentication)
                .ifPresentOrElse(this::setAuthentication, SecurityContextHolder::clearContext);
        filterChain.doFilter(request, response);
    }

    private Optional<DeviceRegisterToken> extractDeviceRegisterToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("bearer "))
                .map(header -> header.substring(7))
                .flatMap(deviceRegisterTokenSerializer::deserialize);
    }

    private JwtAuthentication createAuthentication(DeviceRegisterToken deviceRegisterToken){
        return new JwtAuthentication(
                deviceRegisterToken.getMemberId(),
                Collections.singletonList(new SimpleGrantedAuthority(AccountType.USER.name()))
        ); //TODO: ROLE
    }
    private void setAuthentication(JwtAuthentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
