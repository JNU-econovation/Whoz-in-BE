package com.whoz_in.main_api.shared.presentation.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpRequestLogger {
    public void log(HttpServletRequest request, String requesterId) {
        log.info("[요청] requester id: {}, url: {}, method: {}",
                requesterId,
                request.getRequestURI(),
                request.getMethod()
                );
    }
}
