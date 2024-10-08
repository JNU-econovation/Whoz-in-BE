package com.whoz_in.api.shared.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class SuccessBody<D> extends ApiResponseBody {
    private final D data;
    private final String message;
}
