package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 처리되지 않은 모든 예외를 처리한다.
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UnhandledExceptionHandler {
    private final HttpRequestInfoExtractor httpRequestInfoExtractor;

    // 나머지 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureBody> handleException(Exception e, HttpServletRequest request) {
        log.error("처리하지 못한 예외\n-예외 정보-\n클래스: {}\n메세지: {}\n-요청 정보-\n{}\n-스택 트레이스-",
                e.getClass().getName(),
                e.getMessage(),
                httpRequestInfoExtractor.extractInfoFrom(request),
                e);
        return ResponseEntityGenerator.fail("UNEXPECTED_ERROR", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
