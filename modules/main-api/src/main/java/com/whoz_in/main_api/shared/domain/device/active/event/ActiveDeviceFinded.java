package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.main_api.shared.event.Event;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ActiveDeviceFinded implements Event {

    private final List<UUID> devices;
    // 모니터 로그는 ActiveDevice 가 언제 Active 되었는지 시간을 삽입하기 위해 필요하다.
//    private final List<MonitorLog> logs;

    public ActiveDeviceFinded(List<UUID> devices) {
        this.devices = devices;
    }

}
