package com.whoz_in.network_api.system_validator;


import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//which로 커맨드가 설치되어있는지 확인함
@Component
@RequiredArgsConstructor
public class CommandsInstalledValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) target;
        commands.forEach(command -> {
            List<String> results = TransientProcess.create("which " + command).results();
            if (results.isEmpty() || !results.get(0).contains("/")) {
                errors.reject("command.notInstalled", new Object[]{command}, command + "가 설치되지 않았습니다.");
            }
        });
    }
}
