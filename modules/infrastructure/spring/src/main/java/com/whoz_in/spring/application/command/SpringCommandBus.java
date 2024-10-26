package com.whoz_in.spring.application.command;

import com.whoz_in.api.shared.application.command.Command;
import com.whoz_in.api.shared.application.command.CommandBus;
import com.whoz_in.api.shared.application.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
스프링 컨텍스트 내의 핸들러를 찾아 바로 호출하도록 구현되었으므로 동기적으로 동작합니다.
 */
@Component
@RequiredArgsConstructor
public final class SpringCommandBus implements CommandBus {
    private final CommandHandlers handlers;

    @Override
    public void dispatch(Command command){
        CommandHandler handler = handlers.findBy(command);
        handler.handle(command);
    }
}

