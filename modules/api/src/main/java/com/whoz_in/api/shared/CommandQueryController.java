package com.whoz_in.api.shared;

import com.whoz_in.domain.shared.domain.bus.command.Command;
import com.whoz_in.domain.shared.domain.bus.command.CommandBus;
import com.whoz_in.domain.shared.domain.bus.query.Query;
import com.whoz_in.domain.shared.domain.bus.query.QueryBus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommandQueryController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    protected void dispatch(Command command){
        commandBus.dispatch(command);
    }

    protected <R> R ask(Query query){
        return queryBus.ask(query);
    }
}