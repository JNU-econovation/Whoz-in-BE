package com.whoz_in.main_api.shared.application.command;

/*
우리의 새로운 서비스의 기능을 추가하고 싶을 때마다 이 핸들러와 적절한 Command를 구현하면 된다.
 */
public abstract class CommandHandler<C extends Command>{
    //TODO: 결과의 메타 데이터를 가지는 CommandResult를 반환하도록
    public abstract void handle(C command);
}
