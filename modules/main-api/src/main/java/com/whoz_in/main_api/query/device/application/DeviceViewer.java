package com.whoz_in.main_api.query.device.application;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.UUID;

public interface DeviceViewer extends Viewer {
    //device들의 요약 정보를 반환
    DevicesStatus findDevicesStatus(UUID ownerId);
    // 사용자가 등록한 device 개수를 반환
    DeviceCount countDevice(UUID ownerId);
}
