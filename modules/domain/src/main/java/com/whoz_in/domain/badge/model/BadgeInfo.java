package com.whoz_in.domain.badge.model;

import com.whoz_in.domain.member.model.MemberId;
import java.awt.Color;
import java.util.Random;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeInfo {
    private final String name;
    private final BadgeType badgeType;
    private final MemberId creator;
    private final String colorCode;

    public static BadgeInfo create(String name, BadgeType badgeType, String colorCode,MemberId creator) {
        return new BadgeInfo(name,badgeType,creator,colorCode);
    }

    public static BadgeInfo load(String name, BadgeType badgeType, MemberId creator, String colorCode) {
        return new BadgeInfo(name,badgeType,creator,colorCode);
    }
}
