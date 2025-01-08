package com.whoz_in.main_api.shared.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MainApiGlobalExceptionHandler {
    // 사용자 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureBody> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[USER_ERROR] {}", e.getMessage());
        e.printStackTrace();
        return ResponseEntityGenerator.fail("USER_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 개발자 예외
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<FailureBody> handleIllegalStateException(IllegalStateException e) {
        log.error("[SYSTEM_ERROR] {}", e.getMessage());
        e.printStackTrace();
        return ResponseEntityGenerator.fail("SYSTEM_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 나머지 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureBody> handleException(Exception e) {
        log.error("[UNEXPECTED_ERROR] {}", e.getMessage());
        e.printStackTrace();
        return ResponseEntityGenerator.fail("UNEXPECTED_ERROR", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
