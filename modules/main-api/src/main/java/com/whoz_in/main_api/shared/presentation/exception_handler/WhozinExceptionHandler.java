package com.whoz_in.main_api.shared.presentation.exception_handler;

import com.whoz_in.main_api.command.device.application.DeviceInfoTempAddFailedException;
import com.whoz_in.main_api.shared.presentation.response.FailureBody;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import com.whoz_in.shared.WhozinException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 후즈인에서 정의한 예외를 처리한다.

@Slf4j
@Order(1)
@RestControllerAdvice
@RequiredArgsConstructor
public class WhozinExceptionHandler {
    private final RequesterInfo requesterInfo;

    @ExceptionHandler(DeviceInfoTempAddFailedException.class)
    public ResponseEntity<FailureBody> handle(DeviceInfoTempAddFailedException e) {
        log.error("기기 맥 주소 등록 실패:\n요청자: {}\nip: {}", requesterInfo.getMemberId().toString(), e.getIp());
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WhozinException.class)
    public ResponseEntity<FailureBody> handle(WhozinException e) {
        log.warn(e.getMessage());
        //TODO: http status를 예외에 따라 다르게 설정할 수 있어야 함 - 매핑 클래스를 만들든지 여러 예외 핸들러를 두든지
        return ResponseEntityGenerator.fail(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
