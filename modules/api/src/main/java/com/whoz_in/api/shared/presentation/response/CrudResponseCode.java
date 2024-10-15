package com.whoz_in.api.shared.presentation.response;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CrudResponseCode implements ApiResponseCode {
    CREATE("생성 성공", HttpStatus.CREATED),
    READ("조회 성공", HttpStatus.OK),
    UPDATE("수정 성공", HttpStatus.OK),
    DELETE("삭제 성공", HttpStatus.NO_CONTENT);

    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
