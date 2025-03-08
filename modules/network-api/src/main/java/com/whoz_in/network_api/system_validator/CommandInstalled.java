package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.validation.ValidationRequest;
import java.util.List;

public record CommandInstalled (
        List<String> commands
) implements ValidationRequest {}
