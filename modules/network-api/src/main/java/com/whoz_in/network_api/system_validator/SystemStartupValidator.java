package com.whoz_in.network_api.system_validator;


import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

//시스템의 전체적인 검증을 진행하는 클래스
@Slf4j
@Component
public final class SystemStartupValidator {
    //서버 시작 시 검증 (실패 시 예외가 발생하면시작이 실패되도록 함)
    public SystemStartupValidator(
            NetworkInterfaceProfileConfig profileConfig,
            CommandsInstalledValidator commandsInstalledValidator,
            NetworkInterfaceConnectedValidator networkInterfaceConnectedValidator
    ) {
        log.info("시스템 초기 검증을 수행합니다");

        //커맨드 설치 여부 검증
        List<String> commands = List.of("tshark", "arp-scan", "iwconfig", "nmcli", "iw", "ip");
        Errors errors = commandsInstalledValidator.validateObject(commands);
        if (errors.hasErrors()){
            errors.getAllErrors().forEach(e->System.out.println((String)e.getArguments()[0]));
        }

        // 설정된 네트워크 인터페이스 상태 검증
//        networkInterfaceStateValidator.validate(config.getAllNIs());

        // TODO: 네트워크 인터페이스 상태 검증
        log.info("시스템 초기 검증 완료");
    }

}