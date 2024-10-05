package api.shared;

import domain.shared.domain.bus.command.Command;
import domain.shared.domain.bus.command.CommandBus;
import domain.shared.domain.bus.query.Query;
import domain.shared.domain.bus.query.QueryBus;
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