package com.whoz_in.main_api.command.shared.application;

/*
우리의 새로운 서비스의 기능을 추가하고 싶을 때마다 이 핸들러와 적절한 Command를 구현하면 된다.
 */
public interface CommandHandler<C extends Command, R>{
    R handle(C command);
}
