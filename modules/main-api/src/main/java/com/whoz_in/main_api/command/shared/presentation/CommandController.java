package com.whoz_in.main_api.command.shared.presentation;

import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.query.shared.application.Query;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.application.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommandController {
    private final CommandBus commandBus;

    protected void dispatch(Command command){
        commandBus.dispatch(command);
    }
}