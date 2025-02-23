package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.validation.BiValidator;
import com.whoz_in.network_api.common.validation.ValidationResult;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//세팅된 NetworkInterface들이 시스템에 존재하는 NetworkInterface인지 확인
@Component
@RequiredArgsConstructor
public class NetworkInterfaceValidator implements BiValidator<List<NetworkInterface>, List<NetworkInterface>> {

    @Override
    public ValidationResult getValidationResult(List<NetworkInterface> system, List<NetworkInterface> setting) {
        ValidationResult validationResult = new ValidationResult();

        List<NetworkInterface> unmatchedNIs = setting.stream()
                .filter(ni -> !system.contains(ni))
                .toList();

        if (!unmatchedNIs.isEmpty()) {
            validationResult.addError(
                    "아래 네트워크 인터페이스가 시스템에 존재하지 않거나 상태가 올바르지 않습니다: \n" +
                            unmatchedNIs.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining("\n"))
            );
        }
        return validationResult;
    }
}
