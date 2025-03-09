package com.whoz_in.network_api.system.validation;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.validation.CustomValidator;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.system.routing_table.RtTables;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class RtTablesValidator extends CustomValidator<RtTableValidation> {
    private final NetworkInterfaceProfileConfig config;
    private final NetworkInterfaceManager manager;
    private final RtTables rtTables;

    @Override
    public void validate(RtTableValidation target, Errors errors) {
        List<NetworkInterface> wirelessInterfaces = config.getManagedProfiles().stream()
                .map(profile -> manager.get().get(profile.interfaceName()))
                .filter(NetworkInterface::isWireless)
                .toList();
        if (rtTables.get().size() != wirelessInterfaces.size())
            errors.reject(
                    "rtTables.invalidSize",
                    "rt_tables에 설정된 테이블 개수(%d)와 무선 managed 인터페이스의 개수(%d)가 다릅니다.".formatted(rtTables.get().size(), wirelessInterfaces.size())
            );
    }
}
