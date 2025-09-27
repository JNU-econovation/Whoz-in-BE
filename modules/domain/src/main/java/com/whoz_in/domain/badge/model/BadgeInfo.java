package com.whoz_in.domain.badge.model;

import com.whoz_in.domain.member.model.MemberId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeInfo {
    private final String name;
    private final BadgeType badgeType;
    private final MemberId creator;
    private final String colorString;
    private final String description;

    public static BadgeInfo create(String name, BadgeType badgeType, MemberId creator, String colorCode, String description) {
        return new BadgeInfo(name,badgeType,creator,colorCode, description);
    }

    public static BadgeInfo load(String name, BadgeType badgeType, MemberId creator, String colorCode, String description) {
        return new BadgeInfo(name,badgeType,creator,colorCode, description);
    }
}
