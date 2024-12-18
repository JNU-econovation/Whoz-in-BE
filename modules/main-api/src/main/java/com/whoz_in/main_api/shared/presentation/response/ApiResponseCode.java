package com.whoz_in.main_api.shared.presentation.response;

import org.springframework.http.HttpStatus;

public interface ApiResponseCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
