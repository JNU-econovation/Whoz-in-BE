package com.whoz_in.network_log.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

public class ApiResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessBody<T> {
        private int status;
        private String message;
        private T data;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailureBody {
        private int status;
        private String message;
    }

    public static <T> ApiResponse.SuccessBody success(HttpStatus status,
                                                  String message,
                                                  T data) {
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
