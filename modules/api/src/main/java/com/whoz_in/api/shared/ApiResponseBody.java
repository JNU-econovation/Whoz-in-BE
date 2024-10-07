package com.whoz_in.api.shared;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiResponseBody {
    @Getter
    @AllArgsConstructor
    public static final class FailureBody implements Serializable {
        private String status;
        private String code;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    public static final class SuccessBody<D> implements Serializable {
        private D data;
        private String message;
        private String code;
    }
}
