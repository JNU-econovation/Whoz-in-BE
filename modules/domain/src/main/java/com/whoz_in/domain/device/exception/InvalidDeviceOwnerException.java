package com.whoz_in.domain.device.exception;

import com.whoz_in.domain.shared.BusinessException;

public class InvalidDeviceOwnerException extends BusinessException {
    private InvalidDeviceOwnerException(String errorMessage) {
        super("3020", errorMessage);
    }

    public static InvalidDeviceOwnerException of(String ownerName){
        return new InvalidDeviceOwnerException(String.format("이 기기는 %s 회원의 기기입니다. 해당 회원에게 기기 삭제를 부탁하세요.", ownerName));
    }
}
