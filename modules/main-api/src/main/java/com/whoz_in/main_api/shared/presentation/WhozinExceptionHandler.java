package com.whoz_in.main_api.shared.presentation;

import com.whoz_in.domain.shared.BusinessException;
import com.whoz_in.main_api.shared.application.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 후즈인에서 정의한 예외를 처리한다.

@Slf4j
@Order(1)
@RestControllerAdvice
@RequiredArgsConstructor
public class WhozinExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<FailureBody> handle(ApplicationException e) {
        log.warn(e.getMessage());
        //TODO: http status 예외에 따라 다르게 설정할 수 있어야 함
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //TODO: ApplicationException과 WhozinException으로 합치기
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<FailureBody> handle(BusinessException e) {
        log.warn(e.getMessage());
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
