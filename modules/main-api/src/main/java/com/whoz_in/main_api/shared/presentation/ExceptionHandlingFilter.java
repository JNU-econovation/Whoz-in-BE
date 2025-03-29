package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlingFilter extends OncePerRequestFilter {

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
