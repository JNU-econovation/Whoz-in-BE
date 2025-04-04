package com.whoz_in.domain.network_log;

import com.whoz_in.shared.WhozinException;

public class NoMonitorLogException extends WhozinException {
    public NoMonitorLogException(String mac) {
        super("3043", "%s의 monitor log가 없음".formatted(mac));
    }
}
