package com.whoz_in.main_api.shared.domain.device;

import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;

public class DeviceInfoFixture {

    private static final String room = "testRoom";
    private static final String ssid = "testSsid";

    public static DeviceInfo createMockDeviceInfo(){
        return DeviceInfo.create(
                room,
                ssid,
                MacAddress.create(randomMac())
        );
    }

    private static String randomMac(){
        String result;
        result = String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256));
        return result;
    }

}
