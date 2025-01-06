package com.whoz_in.main_api.shared;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.config.security.JwtAuthentication;
import com.whoz_in.main_api.shared.utils.SpringSecurityRequesterInfo;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

public class RequesterInfoTest {

    private SpringSecurityRequesterInfo requesterInfo = new SpringSecurityRequesterInfo();

    @BeforeEach
    void init(){
        MemberId memberId = new MemberId();
        System.out.println("넣을 memberId : " + memberId);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(memberId, Collections.emptyList()));
    }

    @Test
    @DisplayName("RequesterInfo 등록 테스트")
    void RequesterInfo등록(){
        MemberId memberId = requesterInfo.getMemberId();
        System.out.println("추출된 memberId : " + memberId);
    }


}
