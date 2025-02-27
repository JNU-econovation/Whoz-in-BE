package com.whoz_in.main_api.config;

import com.whoz_in.main_api.shared.presentation.HttpRequestInfoExtractor;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionLoggingFilter implements Filter {
    private final HttpRequestInfoExtractor httpRequestInfoExtractor;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logRequestDetails(req, e);
            throw e;
        }
    }

    private void logRequestDetails(HttpServletRequest request, Exception e) {
        log.error("필터에서 예외가 발생했습니다!\n-예외 정보-\n클래스: {}\n메세지: {}\n-요청 정보-\n{}\n-스택 트레이스-", e.getClass().getName(), e.getMessage(), httpRequestInfoExtractor.extractInfoFrom(request), e);
    }
}
//필터에서 발생하는 예외를 기록합니다
