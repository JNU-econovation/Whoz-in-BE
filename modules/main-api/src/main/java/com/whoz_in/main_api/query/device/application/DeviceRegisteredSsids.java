package com.whoz_in.main_api.query.device.application;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.List;

public record DeviceRegisteredSsids(
    List<String> ssids
) implements View {}
