package com.whoz_in.api_query_jpa.device.connection;

import com.whoz_in.main_api.shared.utils.TimeRanges;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceConnectionUtil {

    // 아직 끊기지 않은 연결의 경우 disconnectedAt이 null이기 때문에 end를 넣어야 한다.
    public static TimeRanges toTimeRanges(List<DeviceConnection> connections, LocalDateTime end) {
        List<TimeRanges.TimeRange> ranges = connections.stream()
                .map(conn -> new TimeRanges.TimeRange(
                        conn.getConnectedAt(),
                        conn.getDisconnectedAt() == null ? end : conn.getDisconnectedAt()
                ))
                .toList();
        return new TimeRanges(ranges);
    }
}
