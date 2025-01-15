package com.whoz_in.main_api.query.device.view;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.UUID;

public interface DeviceInfoViewer extends Viewer {
    RegisteredSsids findRegisteredSsids(UUID ownerId, String room, String mac);
}
