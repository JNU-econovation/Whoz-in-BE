package com.whoz_in.api.shared.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class FailureBody extends ApiResponseBody{
    private final String errorCode;
    private final String message;
}
