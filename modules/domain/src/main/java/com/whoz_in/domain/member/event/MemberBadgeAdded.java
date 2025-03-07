package com.whoz_in.domain.member.event;


import com.whoz_in.domain.shared.event.DomainEvent;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBadgeAdded extends DomainEvent {
    private final String memberId;
    private final Map<String, Boolean> badges;
//    private final UUID memberId; // 멤버 아이디
//    private final Map<UUID, String> badges; // 멤버의 뱃지 소유록
}
