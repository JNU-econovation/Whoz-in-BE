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

//필터에서 발생하는 예외를 기록합니다
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
        log.error("[요청 정보] {} {}\n{}", request.getMethod(), request.getRequestURL(), request.getHeader("User-Agent"));
    }
}
