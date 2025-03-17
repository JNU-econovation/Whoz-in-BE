package com.whoz_in.main_api.shared.domain.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.member.model.MemberId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DeviceFixture {

    public static Device testDevice(UUID ownerId) {
        return Device.create(
                new MemberId(ownerId),
                randomDeviceInfo(),
                "test_device"
        );
    }

    private static Set<DeviceInfo> randomDeviceInfo(){
        Set<DeviceInfo> deviceInfos = new HashSet<>();

        deviceInfos.add(DeviceInfoFixture.createMockDeviceInfo());
        deviceInfos.add(DeviceInfoFixture.createMockDeviceInfo());
        deviceInfos.add(DeviceInfoFixture.createMockDeviceInfo());

        return deviceInfos;
    }

}
