package com.whoz_in.main_api.query.device.application.active.view;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;
import java.util.Optional;

public interface ActiveDeviceViewer extends Viewer {

    Optional<ActiveDevice> findByDeviceId(String deviceId);
    List<ActiveDevice> findAll();
    default ActiveDevice getByDeviceId(String deviceId) {
        return findByDeviceId(deviceId).orElseThrow(()->new IllegalArgumentException("No ActiveDevice Find"));
    }

    List<ActiveDevice> findByDeviceIds(List<String> deviceIds);

}
