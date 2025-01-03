package com.whoz_in.domain.member.model;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Position {
    AI("ai"),
    BE("be"),
    DE("de"),
    FE("fe"),
    PM("pm"),
    GAME("game");

    private String position;

    Position(String position) {
        this.position = position;
    }

    public static Position findPosition(String position){
        return Arrays.stream(Position.values())
                .filter(pos -> pos.getPosition().equals(position))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no position"));
    }
}
