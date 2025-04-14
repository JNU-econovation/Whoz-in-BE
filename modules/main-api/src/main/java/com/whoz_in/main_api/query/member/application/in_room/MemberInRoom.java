package com.whoz_in.main_api.query.member.application.in_room;

import com.whoz_in.main_api.query.shared.application.Response;

public record MemberInRoom(
    String memberId,
    int generation,
    String memberName,
    String mainBadgeName,
    String mainBadgeColor,
    String todayActiveTime,
    boolean isActive
) implements Response {}
