package com.whoz_in.main_api.config;

import com.whoz_in.main_api.shared.presentation.HttpRequestInfoExtractor;
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

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE+1) // 캐싱 먼저 수행
public class ExceptionLoggingFilter extends OncePerRequestFilter {
    private final HttpRequestInfoExtractor httpRequestInfoExtractor;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logRequestDetails(request, e);
            throw e;
        }
    }

    private void logRequestDetails(HttpServletRequest request, Exception e) {
        log.error("필터에서 예외가 발생했습니다!\n-예외 정보-\n클래스: {}\n메세지: {}\n-요청 정보-\n{}\n-스택 트레이스-",
                e.getClass().getName(), e.getMessage(), httpRequestInfoExtractor.extractInfoFrom(request), e);
    }
}
