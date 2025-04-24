package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.shared.application.Response;

public record MemberInRoomResponse(
    int generation,
    String memberId,
    String memberName,
    String continuousActiveTime,
    String totalActiveTime,
    Badge badge,
    long dailyActiveMinute,
    boolean isActive
) implements Response {

    public static MemberInRoomResponse nonDeviceRegisterer(int generation, String memberId, String memberName, BadgeInfo badgeInfo) {
        return new MemberInRoomResponse(
                generation,
                memberId,
                memberName,
                "0시간 0분",
                "0시간 0분",
                new Badge(badgeInfo),
                0,
                false
        );
    }

    // 이 응답 내에서만 사용하는 레코드
    public record Badge(
            String badgeName,
            String colorCode
    ){
        public Badge(BadgeInfo badge) {
            this(badge.name(), badge.colorString());
        }
    }
}
