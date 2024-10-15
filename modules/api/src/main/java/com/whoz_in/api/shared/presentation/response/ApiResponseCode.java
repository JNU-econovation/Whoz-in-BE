package com.whoz_in.api.shared.presentation.response;

import org.springframework.http.HttpStatus;

public interface ApiResponseCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
