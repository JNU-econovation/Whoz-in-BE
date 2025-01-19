package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.shared.application.Response;

public record MemberInRoomResponse(
    int generation,
    String memberId,
    String memberName,
    String continuousActiveTime,
    String totalActiveTime,
    boolean isActive
) implements Response {
}
