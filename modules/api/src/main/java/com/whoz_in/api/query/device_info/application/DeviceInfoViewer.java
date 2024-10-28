package com.whoz_in.api.query.device_info.application;


import com.whoz_in.api.shared.application.query.Viewer;

public interface DeviceInfoViewer extends Viewer {
    DeviceInfo findByDeviceId(Long deviceId);
    DevicesInfo findAll();
}
