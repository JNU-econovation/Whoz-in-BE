package shared.domain.bus.command;

/*
받은 Command에 따라 적절한 CommandHandler에게 명령을 전달하는 인터페이스.
적절한 CommandHandler를 찾는 로직은 이 구현체가 담당해야 한다.
 */
public interface CommandBus {
    void dispatch(Command command);
}
