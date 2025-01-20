package com.whoz_in.main_api.shared.domain.member.event;

import com.whoz_in.main_api.shared.event.Event;
import java.util.UUID;

// TODO: 이벤트 이름 다시 생각해보기
public record MemberCreatedEvent(UUID memberId) implements Event {

}
