package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.validation.CustomValidator;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class NetworkInterfaceExistValidator extends CustomValidator<NetworkInterfaceExist> {
    private final NetworkInterfaceProfileConfig config;
    private final NetworkInterfaceManager manager;

    @Override
    public void validate(NetworkInterfaceExist target, Errors errors) {
        List<String> list = manager.get().stream().map(NetworkInterface::getName).toList();
        config.getAllProfiles().forEach(profile -> {
            if (!list.contains(profile.interfaceName()))
                errors.reject(
                        "networkInterface.notExists",
                        new Object[]{profile.interfaceName()},
                        "%s 네트워크 인터페이스가 존재하지 않습니다.".formatted(profile.interfaceName())
                );
        });
    }
}
