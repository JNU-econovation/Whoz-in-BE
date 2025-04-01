package com.whoz_in.main_api.shared.presentation.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 요청의 기본 정보 로깅
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpRequestLogger {
    public void log(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        log.info("[HTTP REQUEST] ip: {}, url: {}({})",
                clientIp,
                request.getRequestURI(),
                request.getMethod()
        );
    }
}
