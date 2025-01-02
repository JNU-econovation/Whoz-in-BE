package com.whoz_in.main_api.query.member.application;

import com.whoz_in.domain.member.service.PasswordEncoder;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberLoginHandler implements QueryHandler<MemberLogin, MemberLoginResponse> {
    private final MemberViewer viewer;
    private final PasswordEncoder encoder;

    @Override
    public MemberLoginResponse handle(MemberLogin query) {
//            //TODO: query 예외로
        MemberAuthInfo authInfo = viewer.findAuthInfoByLoginId(query.loginId())
                .orElseThrow(()->new RuntimeException("회원이 없음"));
        if (!encoder.matches(query.password(), authInfo.encodedPassword()))
            throw new RuntimeException("로그인에 실패했습니다.");
        return new MemberLoginResponse(authInfo.memberId());
    }
}
