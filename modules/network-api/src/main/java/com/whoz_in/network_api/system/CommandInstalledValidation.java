package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.validation.ValidationRequest;
import java.util.List;

public record CommandInstalledValidation(
        List<String> commands
) implements ValidationRequest {}
