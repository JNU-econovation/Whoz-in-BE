package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//NetworkInterface가 연결됐는지 확인
@Component
@RequiredArgsConstructor
public class NetworkInterfaceConnectedValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object profiles, Errors errors) {
        @SuppressWarnings("unchecked")
        List<NetworkInterface> nis = (List<NetworkInterface>) profiles;

        nis.forEach(ni-> {
            if (ni.isConnected()) return;
            errors.reject(
                "networkInterface.notConnected",
                new Object[]{ni},
                 "%s의 연결이 끊겼습니다.".formatted(ni.getName())
            );
        });
    }
}
