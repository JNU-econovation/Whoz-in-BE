package com.whoz_in.network_api.common.network_interface.nonLinux;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NonLinuxCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
