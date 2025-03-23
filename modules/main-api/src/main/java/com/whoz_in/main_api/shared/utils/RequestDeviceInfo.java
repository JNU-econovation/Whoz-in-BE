package com.whoz_in.main_api.shared.utils;

import com.whoz_in.main_api.shared.enums.DeviceType;

public interface RequestDeviceInfo {
    DeviceType getDeviceType();
    String getOs();
}
