package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.shared.application.ApplicationException;
import lombok.Getter;

@Getter
public class DeviceInfoTempAddFailedException extends ApplicationException {
    private final String ip;
    public DeviceInfoTempAddFailedException(String ip) {
        super("3033", "알 수 없는 오류로 와이파이 등록에 실패했습니다. 관리자에게 문의해주세요.");
        this.ip = ip;
    }
}
