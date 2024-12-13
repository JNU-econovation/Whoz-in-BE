package com.whoz_in.log_writer.system_validator;


import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.common.IwconfigNetworkInterfaces;
import com.whoz_in.log_writer.common.SystemNetworkInterfaces;
import com.whoz_in.log_writer.common.validation.ValidationResult;
import com.whoz_in.log_writer.config.NetworkConfig;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//시스템의 전체적인 검증을 진행하는 클래스
//SystemValidatorConfig에 의해 빈으로 등록됩니다
@Slf4j
@Profile("prod")
@Component
public final class SystemValidator {
    private final NetworkConfig config;
    private final SystemNetworkInterfaces systemNIs;
    private final NetworkInterfaceValidator networkInterfaceValidator;

    //서버 시작 시 검증 (실패 시 예외가 발생하여 서버 시작이 실패하게 됨)
    public SystemValidator(
            NetworkConfig config,
            SystemNetworkInterfaces systemNIs,
            CommandInstalledValidator commandInstalledValidator,
            NetworkInterfaceValidator networkInterfaceValidator) {
        this.config = config;
        this.systemNIs = systemNIs;
        this.networkInterfaceValidator = networkInterfaceValidator;

        log.info("시스템 검증을 수행합니다");

        //커맨드 설치 여부 검증
        List<String> commands = List.of("tshark", "arp-scan", "iwconfig", "nmcli");
        commands.forEach(commandInstalledValidator::validate);

        //네트워크 인터페이스 정보
        List<NetworkInterface> system = systemNIs.getLatest();
        List<NetworkInterface> setting = config.getNetworkInterfaces();

        //네트워크 인터페이스 출력
        log.info("\n시스템 네트워크 인터페이스 - \n{}\n설정된 네트워크 인터페이스 - \n{}",
                system.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")),
                setting.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));

        //네트워크 인터페이스 상태 검증
        networkInterfaceValidator.validate(system, setting);

        log.info("시스템 검증 완료");
    }

    //정기적으로 시스템 상태를 검사함
    //예외를 띄우지 않고 로깅만 하기
    @Scheduled(fixedDelay = 30000)
    private void checkRegularly() {
        log.info("시스템 검증 시작..");
        //네트워크 인터페이스 상태 검증
        ValidationResult result = networkInterfaceValidator.getValidationResult(
                systemNIs.getLatest(), config.getNetworkInterfaces());
        //더 많은 검증하면 result에 추가하기..

        if (result.hasErrors()) {
            log.error("시스템 검증 실패 : [{}]", result);
            return;
        }
        log.info("시스템 검증 완료");
    }

}