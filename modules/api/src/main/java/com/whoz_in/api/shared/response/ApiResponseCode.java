package com.whoz_in.api.shared.response;

import org.springframework.http.HttpStatus;

public interface ApiResponseCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
