package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.shared.application.ApplicationException;
import java.util.List;

// 기기 등록 시 필요한 기기 정보가 하나 이상 빠져있다는 예외
public class MissingSsidsException extends ApplicationException {
    public MissingSsidsException(List<String> missingSsids) {
        super("3041", missingSsids + "가 등록되지 않았습니다.");
    }
}
