package com.whoz_in.main_api.config.security;

import com.whoz_in.main_api.shared.jwt.tokens.OpenApiToken;
import com.whoz_in.main_api.shared.jwt.tokens.OpenApiTokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class OpenApiTokenFilter extends OncePerRequestFilter {
    private final OpenApiTokenSerializer openApiTokenSerializer;

    public OpenApiTokenFilter(OpenApiTokenSerializer openApiTokenSerializer) {
        this.openApiTokenSerializer = openApiTokenSerializer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        extractOpenApiToken(request)
                .map(this::createAuthentication)
                .ifPresentOrElse(this::setAuthentication, SecurityContextHolder::clearContext);
        filterChain.doFilter(request, response);
    }

    private Optional<OpenApiToken> extractOpenApiToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .flatMap(openApiTokenSerializer::deserialize);
    }

    private OpenApiAuthentication createAuthentication(OpenApiToken openApiToken) {
        return new OpenApiAuthentication(
                Collections.singletonList(new SimpleGrantedAuthority("OPEN_API"))
        );
    }

    private void setAuthentication(OpenApiAuthentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
