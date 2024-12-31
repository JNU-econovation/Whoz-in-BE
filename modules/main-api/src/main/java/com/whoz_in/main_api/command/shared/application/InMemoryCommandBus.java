package com.whoz_in.main_api.command.shared.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class InMemoryCommandBus implements CommandBus {
    private final CommandHandlers handlers;

    @Override
    public void dispatch(Command command){
        CommandHandler handler = handlers.findBy(command);
        handler.handle(command);
    }
}

