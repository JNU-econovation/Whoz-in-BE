package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoomGet(
        int page,
        int size,
        String sortType,
        String status
) implements Query {

    public MembersInRoomGet {
        validateParameter(page, size, sortType, status);
    }

    private void validateParameter(int page, int size, String sortType, String status) {
        if(page < 1) throw new IllegalArgumentException("잘못된 파라미터 : page");
        if(size < 1) throw new IllegalArgumentException("잘못된 파라미터 : size");
        if(status!=null && !status.equals("active") && !status.equals("inactive")) throw new IllegalArgumentException("잘못된 파라미터 : status");
    }

    public static MembersInRoomGet of(int page, int size, String sortType, String status) {
        return new MembersInRoomGet(page, size, sortType, status);
    }

}
