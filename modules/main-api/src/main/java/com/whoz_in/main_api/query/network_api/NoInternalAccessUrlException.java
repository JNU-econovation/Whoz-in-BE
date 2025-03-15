package com.whoz_in.main_api.query.network_api;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class NoInternalAccessUrlException extends ApplicationException {

    public NoInternalAccessUrlException(String room) {
        super("7001", "현재 %s는 이용할 수 없습니다.".formatted(room));
    }
}
