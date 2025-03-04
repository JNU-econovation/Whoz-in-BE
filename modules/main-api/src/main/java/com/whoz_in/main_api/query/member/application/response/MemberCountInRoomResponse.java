package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.main_api.query.shared.application.Response;

public record MemberCountInRoomResponse(
        Long count
) implements Response {
}
