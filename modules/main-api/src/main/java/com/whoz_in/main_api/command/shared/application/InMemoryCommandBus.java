package com.whoz_in.main_api.command.shared.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class InMemoryCommandBus implements CommandBus {
    private final CommandHandlers handlers;

    @Override
    public <R> R dispatch(Command command){
        CommandHandler<Command, R> handler = handlers.findBy(command);
        return handler.handle(command);
    }
}

