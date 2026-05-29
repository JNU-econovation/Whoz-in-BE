package com.whoz_in.main_api.query.api.room_access_url;

import com.whoz_in.shared.WhozinException;

public class NoRoomAccessUrlException extends WhozinException {

    public NoRoomAccessUrlException(String room) {
        super("7001", "현재 %s는 이용할 수 없습니다.".formatted(room));
    }
}
