package com.whoz_in.networklog.application.log;

import com.whoz_in.networklog.application.log.manager.LogManager;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ManagerBeanFinder {

    private final ApplicationContext applicationContext;

    @Getter
    private final Map<String, LogManager> managers;

    public ManagerBeanFinder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.managers = new HashMap<>();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if(beanName.contains("LogManager"))
                managers.put(beanName, (LogManager) applicationContext.getBean(beanName));
        }

    }

}
