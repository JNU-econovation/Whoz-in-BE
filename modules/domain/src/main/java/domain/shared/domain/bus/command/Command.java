package domain.shared.domain.bus.command;

/*
userService.login(String userId)

이 프로젝트에서는 위와 같이 담당하는 서비스와 메서드를 직접 호출하지 않고,
아래와 같이 CommandBus에게 커맨드만을 구성해서 넘기는 방식으로 구현한다.

commandBus.dispatch(new LoginCommand(userId))
 */
public interface Command {

}
