package com.whoz_in.main_api.query.device.view;

import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.UUID;

public interface DeviceViewer extends Viewer {
    //room과 mac을 기반으로 기기를 찾고 기기가 등록한 와이파이들을 반환
    RegisteredSsids findRegisteredSsids(UUID ownerId, String room, String mac);
    //device들의 요약 정보를 반환
    DevicesStatus findDevicesStatus(UUID ownerId);
    // 사용자가 등록한 device 개수를 반환
    DeviceCount findDeviceCount(UUID ownerId);
}
