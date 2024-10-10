package com.whoz_in.domain.shared.domain.utils;

import java.util.Optional;

/*
요청에서 유저의 아이디를 가져오는 기능
인증인가를 구현하는 사람은 스프링 빈으로 구현체를 제공하여야 한다.

TODO: 아래를 고민해보기.
이 구현체는 스프링 시큐리티에 의해 구현될 수도 있고 그냥 내가 직접 만든 필터쪽에서 구현을 할 수도 있을 것이다.
이때, 도메인 모듈을 사용하는 곳은 여러 곳이 있을 수 있는데 해당 인터페이스를 구현하지 않을 곳에서도 이 인터페이스를 알아버린다는게 찝찝하다. 해결법이 있을까?
 */

public interface RequesterInfo {
    Optional<Long> findUserId();
}