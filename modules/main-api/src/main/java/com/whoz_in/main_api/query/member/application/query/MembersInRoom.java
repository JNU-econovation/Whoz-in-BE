package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoom(
        int page,
        int size,
        String sortType
) implements Query {
}
