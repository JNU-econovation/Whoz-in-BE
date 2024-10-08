package com.whoz_in.infra.spring.bus.command;

import com.whoz_in.domain.shared.domain.bus.command.Command;
import com.whoz_in.domain.shared.domain.bus.command.CommandHandler;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/*
Command를 받으면 해당 Command를 처리할 수 있는 CommandHandler를 제공하는 클래스입니다.
스프링 컨텍스트를 이용하여 구현되었습니다.
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
                                Class<? extends CommandHandler> handlerClass = commandHandler.getClass();
                                ParameterizedType paramType = (ParameterizedType) handlerClass.getGenericSuperclass();
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
