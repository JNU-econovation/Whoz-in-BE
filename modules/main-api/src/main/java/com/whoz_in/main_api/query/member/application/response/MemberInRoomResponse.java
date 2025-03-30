package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.main_api.query.badge.application.view.BadgeName;
import com.whoz_in.main_api.query.shared.application.Response;

public record MemberInRoomResponse(
    int generation,
    String memberId,
    String memberName,
    String continuousActiveTime,
    String totalActiveTime,
    BadgeName badge,
    long dailyActiveMinute,
    boolean isActive
) implements Response {

    public static MemberInRoomResponse nonDeviceRegisterer(int generation, String memberId, String memberName, BadgeName badgeName) {
        return new MemberInRoomResponse(
                generation,
                memberId,
                memberName,
                "0시간 0분",
                "0시간 0분",
                badgeName,
                0,
                false
        );
    }
}
