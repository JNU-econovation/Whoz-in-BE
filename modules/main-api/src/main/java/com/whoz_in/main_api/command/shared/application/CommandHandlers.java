package com.whoz_in.main_api.command.shared.application;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/*
Command를 받아 해당 Command를 처리할 수 있는 CommandHandler를 제공하는 클래스입니다.
 */
@Component
public final class CommandHandlers {
    Map<Class<? extends Command>, CommandHandler> commandHandlers;

    //스프링 빈 중 CommandHandler인 것들을 찾고, 꺼내서 쓰기 쉽게 Map으로 변경합니다.
    public CommandHandlers(ApplicationContext context) {
        commandHandlers =
            context.getBeansOfType(CommandHandler.class).values().stream()
                .collect(
                        Collectors.toMap(
                            commandHandler -> {
                                //commandHandler의 프록시 객체를 가져옴 (예시 - @Transactional 사용 시)
                                Class<?> targetClass = AopProxyUtils.ultimateTargetClass(commandHandler);
                                //CommandHandler의 제네릭 타입을 알아낸다.
                                ParameterizedType paramType =
                                        (ParameterizedType) Arrays.stream(targetClass.getGenericInterfaces())
                                                .filter(ParameterizedType.class::isInstance)
                                                .filter(type -> ((ParameterizedType) type).getRawType().equals(CommandHandler.class))
                                                .findAny()
                                                .orElseThrow(() -> new IllegalStateException("CommandHandler에 제네릭 타입이 존재하지 않음"));
                                //CommandHandler의 첫 번째 제네릭 타입 반환
                                return (Class<? extends Command>) paramType.getActualTypeArguments()[0];
                            },
                            commandHandler -> commandHandler
                        )
                );
    }

    //해당 Command를 처리하는 CommandHandler를 찾습니다.
    public CommandHandler findBy(Command command){
        CommandHandler commandHandler = commandHandlers.get(command.getClass());
        if (null == commandHandler) throw new IllegalStateException(command.getClass().getName() + "를 처리할 핸들러를 찾을 수 없습니다.");
        return commandHandler;
    }
}
