package com.whoz_in.main_api.shared.presentation;

import com.whoz_in.main_api.shared.application.command.Command;
import com.whoz_in.main_api.shared.application.command.CommandBus;
import com.whoz_in.main_api.shared.application.query.Query;
import com.whoz_in.main_api.shared.application.query.QueryBus;
import com.whoz_in.main_api.shared.application.query.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommandQueryController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    protected void dispatch(Command command){
        commandBus.dispatch(command);
    }

    /**
     * <p><b>주의</b></p>
     * Response는 추상 클래스가 아닌 인터페이스이기 때문에 아래와 같이 사용해도 경고만 발생하고 컴파일 에러는 발생하지 않습니다. </p>
     *
     * <pre>
     * {@code
     * SomeClass obj = ask(query); //SomeClass는 Response를 구현하지 않음
     * }
     * </pre>
     */
    protected <R extends Response> R ask(Query query){
        return queryBus.ask(query);
    }
}