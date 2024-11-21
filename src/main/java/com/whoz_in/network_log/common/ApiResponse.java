package com.whoz_in.network_log.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ApiResponse {

    @RequiredArgsConstructor
    public static class SuccessBody {
        private final int status;
        private final String message;
        private final Object data;
    }

    @RequiredArgsConstructor
    public static class FailureBody {
        private final int status;
        private final String message;
    }

    public static ApiResponse.SuccessBody success(HttpStatus status,
                                                  String message,
                                                  Object data) {
        return new SuccessBody(status.value(), message, data);
    }

    public static ApiResponse.SuccessBody success(HttpStatus status) {
        return new SuccessBody(status.value(), null, null);
    }

    public static ApiResponse.SuccessBody success(HttpStatus status, String message) {
        return new SuccessBody(status.value(), message, null);
    }

    public static ApiResponse.FailureBody failure(HttpStatus status, String message) {
        return new FailureBody(status.value(), message);
    }


}
