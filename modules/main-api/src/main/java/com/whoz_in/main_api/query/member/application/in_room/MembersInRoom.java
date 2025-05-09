package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record MembersInRoom(
        List<MemberInRoom> members,
        int size
) implements Response {
}
