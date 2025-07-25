package com.whoz_in.main_api.shared.presentation.exception_handler;

import com.whoz_in.main_api.shared.presentation.logging.HttpRequestExceptionLogger;
import com.whoz_in.main_api.shared.presentation.response.FailureBody;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
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
    private final HttpRequestExceptionLogger httpRequestExceptionLogger;

    // 나머지 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureBody> handleException(HttpServletRequest request, Exception e) {
        httpRequestExceptionLogger.log("처리하지 못한 예외", request, e);
        return ResponseEntityGenerator.fail("UNEXPECTED_ERROR", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
