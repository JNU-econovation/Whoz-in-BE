package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoomGet(
        int page,
        int size
) implements Query {

    public MembersInRoomGet {
        if(page < 1) throw new IllegalArgumentException("잘못된 파라미터 : page");
        if(size < 1) throw new IllegalArgumentException("잘못된 파라미터 : size");
    }

}
