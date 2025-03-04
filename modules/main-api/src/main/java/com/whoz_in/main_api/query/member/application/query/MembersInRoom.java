package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.member.application.request.MembersInRoomRequest;
import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoom(int page, int size, String sortType, String status) implements Query {

    public static MembersInRoom of(int page, int size, String sortType, String status) {
        return new MembersInRoom(page, size, sortType, status);
    }

    public static MembersInRoom of(MembersInRoomRequest request) {
        return new MembersInRoom(request.page(), request.size(), request.sortType(), request.status());
    }

}
