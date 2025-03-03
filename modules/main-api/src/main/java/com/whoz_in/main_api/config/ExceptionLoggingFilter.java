package com.whoz_in.main_api.config;

import com.whoz_in.main_api.shared.presentation.ExceptionLogger;
import com.whoz_in.main_api.shared.presentation.HttpRequestInfoExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE+1) // 캐싱 먼저 수행
public class ExceptionLoggingFilter extends OncePerRequestFilter {
    private final ExceptionLogger exceptionLogger;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            exceptionLogger.log("필터에서 예외 발생!", request, e);
            // TODO: 알려진 예외 처리하기 및 예외 응답 생성 클래스 만들기, 이 클래스에서 아래 로직 떼어내기
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error_code\": \"UNEXPECTED_ERROR\", \"message\": \"알 수 없는 예외 발생\"}");
            response.getWriter().flush();
        }
    }
}
