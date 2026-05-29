package com.whoz_in.main_api.query.api.member.application.daily;

import com.whoz_in.main_api.query.api.member.exception.InvalidPageParameterException;
import com.whoz_in.main_api.query.api.member.exception.InvalidSizeParameterException;
import com.whoz_in.main_api.query.shared.application.Query;

public record DailyMembersGet(
        int page,
        int size
) implements Query {

    public DailyMembersGet {
        if(page < 1) throw InvalidPageParameterException.EXCEPTION;
        if(size < 1) throw InvalidSizeParameterException.EXCEPTION;
    }

}
