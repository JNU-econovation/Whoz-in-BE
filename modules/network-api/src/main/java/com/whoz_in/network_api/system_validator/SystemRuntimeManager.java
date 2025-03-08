package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemRuntimeManager {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final NetworkInterfaceManager networkInterfaceManager;
    public void validate(){
        networkInterfaceManager.refresh();
    }
    //정기적으로 시스템 상태를 검사함
    //예외를 띄우지 않고 로깅만 하기
//    @Scheduled(fixedDelay = 30000)
//    private void checkRegularly() {
//        log.info("시스템 검증 시작..");
//        // 검증 에러 담을 객체
//        ValidationResult result = new ValidationResult();
//
//        // TODO: ni를 eth 생각 안하고 설계해서 하드코딩했음 Term3 때 고치겠습니다.
//        ArrayList<NetworkInterface> withoutEth = new ArrayList<>(config.getAllNIs());
//        withoutEth.removeIf(ni-> ni.getName().equals("eth0"));
//
//        //네트워크 인터페이스 상태 검증
//        try {
//            networkInterfaceValidator.validate(systemNIs.resolve(), withoutEth);
//        } catch (ValidationException e){
//            // 검증 후 에러들 추가
//            List<String> errors = e.getValidationResult().getErrors();
//            result.addErrors(errors);
//            // 에러 중에 Monitor 들어있으면 복구 로직(모니터 모드로 변경) 실행 // TODO: 이렇게 문자열로 검사하는 것보단 검증 관련 클래스 다 뜯어 고치는게 맞을듯
//            errors.stream()
//                    .filter(error-> error.contains(config.getMonitorNI().toString()))
//                    .findAny()
//                    .ifPresent((m)->monitorModeSwitcher.execute());
//        }
//        //더 많은 검증하면 result에 추가하기..
//
//        if (result.hasError()) {
//            log.error("시스템 검증 실패 : [{}]", result);
//            return;
//        }
//        log.info("시스템 검증 완료");
//    }
}
