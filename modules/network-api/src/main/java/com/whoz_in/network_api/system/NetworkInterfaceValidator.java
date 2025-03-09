package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.validation.CustomValidator;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class NetworkInterfaceValidator extends CustomValidator<NetworkInterfaceValidation> {
    private final NetworkInterfaceProfileConfig config;
    private final NetworkInterfaceManager manager;

    @Override
    public void validate(NetworkInterfaceValidation target, Errors errors) {
        Map<String, NetworkInterface> systemNiMap = manager.get();
        Set<String> systemNiNames = systemNiMap.keySet();

        //존재 확인
        config.getAllProfiles().forEach(profile -> {
            if (!systemNiNames.contains(profile.interfaceName()))
                errors.reject(
                        "networkInterface.notExists",
                        new Object[]{profile.interfaceName()},
                        "%s 네트워크 인터페이스가 존재하지 않습니다.".formatted(profile.interfaceName())
                );
        });
        //연결 확인
        config.getManagedProfiles().stream()
                .map(profile -> systemNiMap.get(profile.interfaceName()))
                .forEach(ni -> {
                    if (ni.isConnected()) return;
                    errors.reject(
                            "networkInterface.notConnected",
                            new Object[]{ni},
                            "%s의 연결이 끊겨있습니다.".formatted(ni.getName())
                    );
        });
        //모니터 모드 확인
        NetworkInterface monitorInterface = systemNiMap.get(config.getMonitorProfile().interfaceName());
        String mode = monitorInterface.getWirelessInfo().mode();
        if (!mode.equals("monitor")) {
            errors.reject(
                    "monitorInterface.notMonitorMode",
                    new Object[]{monitorInterface},
                    "%s가 모니터 모드가 아닙니다. (현재 %s)".formatted(monitorInterface.getName(), mode)
            );
        }
    }
}
