package com.whoz_in.main_api.shared.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//라이브러리나 프레임워크에서 정의된 예외를 처리한다.
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExternalExceptionHandler {
    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<FailureBody> handle(MissingRequestCookieException e) {
        String missingCookieName = e.getCookieName();
        return ResponseEntityGenerator.fail(
                "MISSING_COOKIE",
                String.format("요청에 필수 쿠키 '%s'가 없습니다.", missingCookieName),
                HttpStatus.BAD_REQUEST
        );
    }

    // 하위 클래스로 처리하지 못했을때
    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<FailureBody> handle(MissingRequestValueException e) {
        log.warn(e.getMessage());
        return ResponseEntityGenerator.fail("MISSING_REQUEST_VALUE", "요청에 값이 비어있습니다.", HttpStatus.BAD_REQUEST);
    }
}
