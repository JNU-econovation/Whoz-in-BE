package domain.shared.domain.bus.command;

/*
헥사고날의 인고잉 포트(서비스가 제공하는 기능)와 비슷한 개념이다.
새로운 서비스의 기능을 추가하고 싶을 때마다 이 핸들러와 적절한 Command를 구현하면 된다.
 */
public interface CommandHandler<C extends Command>{
    void handle(C command);
}
