package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record MembersInRoomResponse(
        List<MemberInRoomResponse> members,
        int size
) implements Response {
}
