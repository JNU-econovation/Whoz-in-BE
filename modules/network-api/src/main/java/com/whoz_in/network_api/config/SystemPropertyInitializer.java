package com.whoz_in.network_api.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// application.yml에 정의된 환경 변수를 시스템 프로퍼티에 등록합니다.
// 스프링 컨텍스트에 등록되지 않은 클래스에서도 application.yml에 정의된 값을 이용할 수 있습니다.
@Component
public class SystemPropertyInitializer {
    public SystemPropertyInitializer(Environment environment) {
        System.setProperty("sudo-password", environment.getProperty("sudo-password", ""));
    }
}