package com.whoz_in.domain.member.model;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Position {
    AI("ai"),
    BE("be"),
    DE("de"), //디자이너
    FE("fe"),
    PM("pm"),
    GAME("game"),
    APP("app");

    private final String name;

    Position(String name) {
        this.name = name;
    }

    public static Position findByName(String position){
        return Arrays.stream(Position.values())
                .filter(pos -> pos.getName().equalsIgnoreCase(position))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no position"));
    }
}
