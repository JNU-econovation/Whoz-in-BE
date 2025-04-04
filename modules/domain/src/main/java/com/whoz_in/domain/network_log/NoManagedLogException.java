package com.whoz_in.domain.network_log;

import com.whoz_in.shared.WhozinException;

public class NoManagedLogException extends WhozinException {
    public NoManagedLogException(String ipOrMac) {
        super("3042", "%s의 managed log가 없음".formatted(ipOrMac));
    }
}
