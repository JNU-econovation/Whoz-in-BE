package com.whoz_in.main_api.shared.presentation;

import com.whoz_in.domain.shared.BusinessException;
import com.whoz_in.main_api.shared.application.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MainApiGlobalExceptionHandler {
    //TODO: mapper
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<FailureBody> handle(ApplicationException e, HttpServletRequest request) {
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //TODO: ApplicationException과 합쳐야 할듯
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<FailureBody> handle(BusinessException e) {
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 나머지 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureBody> handleException(Exception e) {
        log.error("[UNEXPECTED_ERROR] {}", e.getMessage());
        e.printStackTrace();
        return ResponseEntityGenerator.fail("UNEXPECTED_ERROR", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
