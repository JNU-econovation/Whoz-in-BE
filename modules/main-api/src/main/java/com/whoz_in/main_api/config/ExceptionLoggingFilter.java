package com.whoz_in.main_api.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logRequestDetails(req);
            throw e;
        }
    }

    private void logRequestDetails(HttpServletRequest request) {
        log.error("요청 URL: {}", request.getRequestURL());
        log.error("요청 메서드: {}", request.getMethod());
        log.error("User-Agent: {}", request.getHeader("User-Agent"));
    }
}
