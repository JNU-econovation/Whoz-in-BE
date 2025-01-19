package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record MembersInRoomResponse(
        List<MemberInRoomResponse> members
) implements Response {
}
