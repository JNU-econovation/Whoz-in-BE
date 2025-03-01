package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.main_api.query.shared.application.Response;

public record MemberInRoomResponse(
    int generation,
    String memberId,
    String memberName,
    String continuousActiveTime,
    String totalActiveTime,
    boolean isActive
) implements Response {

    public static MemberInRoomResponse nonDeviceRegisterer(int generation, String memberId, String memberName) {
        return new MemberInRoomResponse(
                generation,
                memberId,
                memberName,
                "0시간 0분",
                "0시간 0분",
                false
        );
    }
}
