package com.whoz_in.main_api.shared.application.command;

/*
받은 Command에 따라 적절한 CommandHandler에게 명령을 전달하는 인터페이스이다.
 */
public interface CommandBus {
    void dispatch(Command command);
}
