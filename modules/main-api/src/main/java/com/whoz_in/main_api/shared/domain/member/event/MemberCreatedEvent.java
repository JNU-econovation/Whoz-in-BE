package com.whoz_in.main_api.shared.domain.member.event;

import com.whoz_in.main_api.shared.event.Event;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

// TODO: 이벤트 이름 다시 생각해보기
@RequiredArgsConstructor
public class MemberCreatedEvent implements Event {

    private final UUID memberId;

}
