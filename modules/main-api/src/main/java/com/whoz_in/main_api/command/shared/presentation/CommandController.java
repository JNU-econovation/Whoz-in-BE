package com.whoz_in.main_api.command.shared.presentation;

import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommandController {
    private final CommandBus commandBus;

    protected <R> R dispatch(Command command){
        return commandBus.dispatch(command);
    }
}