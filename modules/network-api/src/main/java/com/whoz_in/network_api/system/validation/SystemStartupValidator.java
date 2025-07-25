package com.whoz_in.network_api.system.validation;


import com.whoz_in.network_api.common.LinuxCondition;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

//시스템의 전체적인 검증을 진행하는 클래스
@Slf4j
@Conditional(LinuxCondition.class)
@Component
public final class SystemStartupValidator {
    //서버 시작 시 검증 (실패 시 예외가 발생하면시작이 실패되도록 함)
    public SystemStartupValidator(
            CommandsInstalledValidator commandsInstalledValidator,
            NetworkInterfaceValidator networkInterfaceValidator
    ) {
        log.info("시스템 초기 검증을 수행합니다");

        //커맨드 설치 여부 검증
        Errors commandErrors = commandsInstalledValidator.validateObject(
                new CommandInstalledValidation(List.of("tshark", "arp-scan", "iwconfig", "nmcli", "iw", "ip"))
        );
        printErrorMessage(commandErrors);

        // 설정된 네트워크 인터페이스 검증
        Errors niErrors = networkInterfaceValidator.validateObject(new NetworkInterfaceValidation());
        printErrorMessage(niErrors);

        if (commandErrors.hasErrors() || niErrors.hasErrors()) {
            log.error("시스템 초기 검증에 실패했습니다. 애플리케이션을 종료합니다.");
            throw new IllegalStateException("검증 실패");
        }

        log.info("시스템 초기 검증 완료");
    }
    private void printErrorMessage(Errors errors){
        if (errors.hasErrors()){
            errors.getAllErrors().forEach(e->log.error(e.getDefaultMessage()));
        }
    }
}
