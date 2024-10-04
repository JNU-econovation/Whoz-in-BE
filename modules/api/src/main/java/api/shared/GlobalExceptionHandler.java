//package com.gyuzal.whozin.shared.infrastructure.presentation;
//
//import com.gyuzal.whozin.shared.shared.BusinessException;
//import com.gyuzal.whozin.shared.infrastructure.presentation.ApiResponseBody.FailureBody;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    //애플리케이션 계층에서
//    @ExceptionHandler(value = {BusinessException.class})
//    protected ResponseEntity<FailureBody> handleConflict(BusinessException e) {
//        log.warn("BusinessException", e);
//        return ResponseEntityGenerator.fail(e.getMessage(), e.getCode(), e.getHttpStatus());
//    }
//}
