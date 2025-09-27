package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.member.exception.InvalidPageParameterException;
import com.whoz_in.main_api.query.member.exception.InvalidSizeParameterException;
import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoomGet(
        int page,
        int size
) implements Query {

    public MembersInRoomGet {
        if(page < 1) throw InvalidPageParameterException.EXCEPTION;
        if(size < 1) throw InvalidSizeParameterException.EXCEPTION;
    }

}
