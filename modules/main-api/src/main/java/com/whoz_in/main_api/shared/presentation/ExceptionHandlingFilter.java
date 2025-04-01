package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 필터에서 발생한 예외나 mvc에서 처리하지 못한 예외를 받아 응답 생성
// 예상치 못한 예외가 떠도 일관적인 응답을 보내기 위한 것.
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class ExceptionHandlingFilter extends OncePerRequestFilter {

    // TODO: 알려진 예외 처리하기 및 응답 생성 클래스 만들기
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (response.isCommitted()) return;
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error_code\": \"UNEXPECTED_ERROR\", \"message\": \"알 수 없는 예외 발생\"}");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
