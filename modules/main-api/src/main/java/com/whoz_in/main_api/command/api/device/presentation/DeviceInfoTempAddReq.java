package com.whoz_in.main_api.command.api.device.presentation;

import com.whoz_in.domain.device.model.IpAddress;
import jakarta.annotation.Nullable;

public record DeviceInfoTempAddReq(
        IpAddress ip,
        @Nullable String ssidHint
) {}
