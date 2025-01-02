package com.whoz_in.api_query_jpa.member;

import com.whoz_in.main_api.query.member.application.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberJpaViewer implements MemberViewer {
    private final MemberRepository repository;

    @Override
    public Optional<MemberAuthInfo> findAuthInfoByLoginId(String loginId) {
        return repository.findByLoginId(loginId)
                .map(member -> new MemberAuthInfo(member.getId(), member.getLoginId(), member.getEncodedPassword()));
    }
}
