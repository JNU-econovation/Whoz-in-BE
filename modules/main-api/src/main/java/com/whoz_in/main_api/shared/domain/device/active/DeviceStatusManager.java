package com.whoz_in.main_api.shared.domain.device.active;

/**
 * Active 또는 InActive 상태인 Device 들을 찾는 친구
 */
public interface DeviceStatusManager {

    void activeDeviceFind();
    void inActiveDeviceFind();

}
