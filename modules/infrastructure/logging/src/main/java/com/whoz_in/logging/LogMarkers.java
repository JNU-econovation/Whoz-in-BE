package com.whoz_in.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogMarkers {

    // 로그 레벨에 상관 없이 알려야 하는 로그
    public static final Marker ALERT = MarkerFactory.getMarker("ALERT");
}
