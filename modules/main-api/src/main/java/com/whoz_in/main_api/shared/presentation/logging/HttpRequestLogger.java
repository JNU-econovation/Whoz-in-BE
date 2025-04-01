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
        log.info("[HTTP REQUEST] ip: {}, url: {}({})",
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod()
        );
    }
}
