package com.whoz_in.network_api.common;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class NonLinuxInProdBlocker {

    @PostConstruct
    public void validateEnvironment() {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("linux")) {
            throw new IllegalStateException(
                    "운영 환경(production)에서는 Linux 운영체제에서만 실행되어야 합니다. 현재 OS: " + os
            );
        }
    }
}
