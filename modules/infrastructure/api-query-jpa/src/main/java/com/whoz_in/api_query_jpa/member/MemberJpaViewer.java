package com.whoz_in.api_query_jpa.member;

import com.whoz_in.main_api.query.member.application.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.util.Optional;

public class MemberJpaViewer implements MemberViewer {

    @Override
    public Optional<MemberAuthInfo> getAuthInfoByLoginId(String loginId) {
        return Optional.empty();
    }
}
