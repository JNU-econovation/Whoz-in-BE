package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.shared.application.Query;
import org.springframework.web.bind.annotation.RequestParam;

public record MembersInRoom(int page, int size, String sortType, String status) implements Query {

    public MembersInRoom(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortType") String sortType,
            @RequestParam("status") String status
    ) {
        this.page = page;
        this.size = size;
        this.sortType = sortType;
        this.status = status;
    }


}
