package com.whoz_in.main_api.query.device.application;

import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class DevicesStatusHandler implements QueryHandler<DevicesStatusGet, DevicesStatus> {
    private final RequesterInfo requesterInfo;
    private final DeviceViewer deviceViewer;

    @Transactional(readOnly = true)
    @Override
    public DevicesStatus handle(DevicesStatusGet query) {
        UUID memberId = requesterInfo.getMemberId().id();
        return deviceViewer.findDevicesStatus(memberId);
    }
}
