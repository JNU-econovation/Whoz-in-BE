package com.whoz_in.main_api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ServerAuthenticationFilter extends OncePerRequestFilter {
    private final String apiKey;

    public ServerAuthenticationFilter(@Value("${api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Optional<String> extractedKey = extractApiKey(request);

        if (extractedKey.filter(apiKey::equals).isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractApiKey(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"));
    }
}
