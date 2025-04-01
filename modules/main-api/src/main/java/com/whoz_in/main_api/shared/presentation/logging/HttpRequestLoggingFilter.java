package com.whoz_in.main_api.shared.presentation.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 http 레벨에서 요청 정보 로깅 <br>
 요청 바디를 로깅하기 위해 <br>
 {@link com.whoz_in.main_api.shared.presentation.RequestBodyCachingFilter} 이후로 등록
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE+2)
public class HttpRequestLoggingFilter extends OncePerRequestFilter {
    private final HttpRequestLogger httpRequestLogger;
    private final HttpRequestExceptionLogger httpRequestExceptionLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            httpRequestLogger.log(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            httpRequestExceptionLogger.log("필터에서 예외 발생!", request, e);
            throw e;
        }
    }
}
