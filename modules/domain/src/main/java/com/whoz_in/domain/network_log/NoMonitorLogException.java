package com.whoz_in.domain.network_log;

import com.whoz_in.domain.shared.BusinessException;

// TODO: WhozinException으로 바꾸기
public class NoMonitorLogException extends BusinessException {
    public NoMonitorLogException(String mac) {
        super("3043", "%s의 monitor log가 없음".formatted(mac));
    }
}