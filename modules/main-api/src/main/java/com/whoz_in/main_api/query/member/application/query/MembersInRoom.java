package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.shared.application.Query;

public record MembersInRoom(
        int page,
        int size,
        String sortType,
        String status
) implements Query {

    public MembersInRoom(int page, int size, String sortType, String status) {
        validateParameter(page, size, sortType, status);
        this.page = page;
        this.size = size;
        this.sortType = sortType;
        this.status = status;
    }

    private void validateParameter(int page, int size, String sortType, String status) {
        if(page < 1) throw new IllegalArgumentException("잘못된 파라미터 : page");
        if(size < 1) throw new IllegalArgumentException("잘못된 파라미터 : size");
        if(status!=null && !status.equals("active") && !status.equals("inactive")) throw new IllegalArgumentException("잘못된 파라미터 : status");
    }

    public static MembersInRoom of(int page, int size, String sortType, String status) {
        return new MembersInRoom(page, size, sortType, status);
    }

}
