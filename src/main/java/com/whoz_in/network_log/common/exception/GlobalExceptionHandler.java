package com.whoz_in.network_log.common.exception;

import com.whoz_in.network_log.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ApiResponse.FailureBody handle(RuntimeException e) {
        return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
