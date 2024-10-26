package com.whoz_in.api.query.device_info.application;


public interface DeviceInfoViewer {
    DeviceInfo findByDeviceId(Long deviceId);
    DevicesInfo findAll();
}
