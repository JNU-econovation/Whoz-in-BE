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
        validateParameter(page, size, sortType, status);
        this.page = page;
        this.size = size;
        this.sortType = sortType;
        this.status = status;
    }

    private void validateParameter(
            int page,
            int size,
            String sortType,
            String status
    ){
        if(page < 1 || size < 1)
            throw new IllegalArgumentException("잘못된 파라미터 : page, size");

        // TODO: 하드코딩 말고 다른 방법 적용하기
        if(!status.equalsIgnoreCase("all") &&
                !status.equalsIgnoreCase("active") &&
                !status.equalsIgnoreCase("inactive")){
            throw new IllegalArgumentException("잘못된 파라미터 : status");
        }

    }


}
