package com.whoz_in.main_api.query.api.device.application;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;

public record DeviceOwner(
        UUID ownerId
    ) implements View {
}
