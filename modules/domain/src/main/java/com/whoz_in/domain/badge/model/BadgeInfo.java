package com.whoz_in.domain.badge.model;

import com.whoz_in.domain.badge.exception.InvalidFormColorCodeException;
import com.whoz_in.domain.member.model.MemberId;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeInfo {
    public static final String COLOR_CODE_REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private final String name;
    private final BadgeType badgeType;
    private final MemberId creator;
    private final String colorCode;
    private final String description;

    public static BadgeInfo create(String name, BadgeType badgeType, MemberId creator, String colorCode, String description) {
        if (!Pattern.matches(COLOR_CODE_REGEX,colorCode)) {
            throw InvalidFormColorCodeException.EXCEPTION;
        }
        return new BadgeInfo(name,badgeType,creator,colorCode, description);
    }

    public static BadgeInfo load(String name, BadgeType badgeType, MemberId creator, String colorCode, String description) {
        return new BadgeInfo(name,badgeType,creator,colorCode, description);
    }
}
