package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionLogger {
    private final HttpRequestInfoExtractor httpRequestInfoExtractor;
    public void log(String explain, HttpServletRequest request, Exception e) {
        // 내 프로젝트 코드 중 예외를 초래한 코드 찾기
        StackTraceElement origin = Arrays.stream(e.getStackTrace())
                .filter(el -> el.getClassName().startsWith("com.whoz_in"))
                .findFirst()
                .orElse(e.getStackTrace()[0]);
        log.error("""
                {}
                -실행 정보-
                클래스: {}
                메서드: {}
                -예외 정보-
                클래스: {}
                메세지: {}
                -요청 정보-
                {}
                -스택 트레이스-
                """,
                explain,
                origin.getClassName(),
                origin.getMethodName(),
                e.getClass().getName(),
                e.getMessage(),
                httpRequestInfoExtractor.extractInfoFrom(request),
                e);
    }
}
