package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.common.validation.ValidationResult;
import com.whoz_in.network_api.common.validation.Validator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//which로 커맨드가 설치되어있는지 확인함
@Component
@RequiredArgsConstructor
public class CommandInstalledValidator implements Validator<String> {

    @Override
    public ValidationResult getValidationResult(String command){
        ValidationResult validationResult = new ValidationResult();

        List<String> results = new TransientProcess("which " + command).resultList();
        if (results.isEmpty() || !results.get(0).contains("/")) {
            validationResult.addError(command + "가 설치되지 않았습니다.");
        }
        return validationResult;
    }
}
