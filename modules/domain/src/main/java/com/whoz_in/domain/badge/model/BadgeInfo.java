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

    public static BadgeInfo create(String name, BadgeType badgeType, MemberId creator) {
        String colorCode = generateColorFromName(name);
        return new BadgeInfo(name,badgeType,creator,colorCode);
    }

    public static BadgeInfo load(String name, BadgeType badgeType, MemberId creator, String colorCode) {
        return new BadgeInfo(name,badgeType,creator,colorCode);
    }

    private static String generateColorFromName(String name) {
        int hash = name.hashCode();
        Random random = new Random(hash);

        float hue = random.nextFloat();
        float saturation = 0.5f + (random.nextFloat() * 0.3f);
        float brightness = 0.8f + (random.nextFloat() * 0.2f);

        Color color = Color.getHSBColor(hue, saturation, brightness);

        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }
}
