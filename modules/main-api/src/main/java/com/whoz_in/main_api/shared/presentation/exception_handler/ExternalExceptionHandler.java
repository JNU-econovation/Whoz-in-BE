package com.whoz_in.main_api.shared.presentation.exception_handler;

import com.whoz_in.main_api.shared.presentation.response.FailureBody;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

//라이브러리나 프레임워크에서 정의된 예외를 처리한다.
@Slf4j
@Order(0)
@RestControllerAdvice
@RequiredArgsConstructor
public class ExternalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<FailureBody> handle(NoResourceFoundException e) {
        return ResponseEntityGenerator.fail(
                "NO_RESOURCE_FOUND",
                e.getMessage(),
                HttpStatus.valueOf(e.getStatusCode().value())
        );
    }

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
