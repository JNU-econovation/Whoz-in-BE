package com.whoz_in.domain.network_log;

import com.whoz_in.domain.shared.BusinessException;

// TODO: WhozinException으로 바꾸기
public class NoManagedLogException extends BusinessException {
    public NoManagedLogException(String ipOrMac) {
        super("3042", "%s의 managed log가 없음".formatted(ipOrMac));
    }
}