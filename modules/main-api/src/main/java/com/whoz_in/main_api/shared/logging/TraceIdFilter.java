package com.whoz_in.main_api.shared.logging;

import com.whoz_in.logging.TraceId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 횡단 관심사라서 특정 계층에 안넣었음
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class TraceIdFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", new TraceId().toString());
        try {
            filterChain.doFilter(request, response); // 이후 로깅은 traceId가 같이 찍힘
        } finally {
            MDC.clear();
        }
    }
}
