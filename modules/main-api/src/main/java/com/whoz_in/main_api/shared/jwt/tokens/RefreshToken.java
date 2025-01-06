package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.MemberId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED) //tokenId를 외부에서 설정할 수 없도록 한다.
public final class RefreshToken extends Token {
    private final MemberId memberId;
    private final UUID tokenId; //블랙 리스트 등에 사용될 수 있음

    public RefreshToken(MemberId memberId) {
        this(memberId, UUID.randomUUID());
    }
}
