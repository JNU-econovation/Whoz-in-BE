package com.whoz_in.network_api.system_validator;


import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.SystemNetworkInterfaces;
import com.whoz_in.network_api.common.validation.ValidationException;
import com.whoz_in.network_api.common.validation.ValidationResult;
import com.whoz_in.network_api.config.NetworkConfig;
import java.util.ArrayList;
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
    private final MonitorModeSwitcher monitorModeSwitcher;

    //서버 시작 시 검증 (실패 시 예외가 발생하여 서버 시작이 실패하게 됨)
    public SystemValidator(
            NetworkConfig config,
            SystemNetworkInterfaces systemNIs,
            CommandInstalledValidator commandInstalledValidator,
            NetworkInterfaceValidator networkInterfaceValidator,
            MonitorModeSwitcher monitorModeSwitcher) {
        this.config = config;
        this.systemNIs = systemNIs;
        this.networkInterfaceValidator = networkInterfaceValidator;
        this.monitorModeSwitcher = monitorModeSwitcher;
        log.info("시스템 검증을 수행합니다");

        //커맨드 설치 여부 검증
        List<String> commands = List.of("tshark", "arp-scan", "iwconfig", "nmcli", "iw", "ip");
        commands.forEach(commandInstalledValidator::validate);

        //네트워크 인터페이스 정보
        List<NetworkInterface> system = systemNIs.getLatest();
        List<NetworkInterface> setting = config.getAllNIs();

        //모니터 모드로 변경
        monitorModeSwitcher.execute();

        //네트워크 인터페이스 출력
        log.info("\n시스템 네트워크 인터페이스 - \n{}\n설정된 네트워크 인터페이스 - \n{}",
                system.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")),
                setting.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));

        // TODO: ni를 eth 생각 안하고 설계해서 하드코딩했음 Term3 때 고치겠습니다.
        ArrayList<NetworkInterface> withoutEth = new ArrayList<>(config.getAllNIs());
        withoutEth.removeIf(ni-> ni.getInterfaceName().equals("eth0"));

        //네트워크 인터페이스 상태 검증
        networkInterfaceValidator.validate(system, withoutEth);

        log.info("시스템 검증 완료");
    }

    //정기적으로 시스템 상태를 검사함
    //예외를 띄우지 않고 로깅만 하기
    @Scheduled(fixedDelay = 30000)
    private void checkRegularly() {
        log.info("시스템 검증 시작..");
        // 검증 에러 담을 객체
        ValidationResult result = new ValidationResult();

        // TODO: ni를 eth 생각 안하고 설계해서 하드코딩했음 Term3 때 고치겠습니다.
        ArrayList<NetworkInterface> withoutEth = new ArrayList<>(config.getAllNIs());
        withoutEth.removeIf(ni-> ni.getInterfaceName().equals("eth0"));

        //네트워크 인터페이스 상태 검증
        try {
            networkInterfaceValidator.validate(systemNIs.getLatest(), withoutEth);
        } catch (ValidationException e){
            // 검증 후 에러들 추가
            List<String> errors = e.getValidationResult().getErrors();
            result.addErrors(errors);
            // 에러 중에 Monitor 들어있으면 복구 로직(모니터 모드로 변경) 실행 // TODO: 이렇게 문자열로 검사하는 것보단 검증 관련 클래스 다 뜯어 고치는게 맞을듯
            errors.stream()
                    .filter(error-> error.contains(config.getMonitorNI().toString()))
                    .findAny()
                    .ifPresent((m)->monitorModeSwitcher.execute());
        }
        //더 많은 검증하면 result에 추가하기..

        if (result.hasError()) {
            log.error("시스템 검증 실패 : [{}]", result);
            return;
        }
        log.info("시스템 검증 완료");
    }

}