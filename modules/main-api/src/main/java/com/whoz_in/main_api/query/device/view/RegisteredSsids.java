package com.whoz_in.main_api.query.device.view;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.List;

public record RegisteredSsids(
    List<String> ssids
) implements View {}
