package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.validation.ValidationRequest;
import java.util.List;

public record NetworkInterfaceExist(
        List<String> system // 시스템에 존재하는 인터페이스
) implements ValidationRequest {}
