package com.whoz_in.main_api.query.member.application.view;

import com.whoz_in.main_api.query.shared.application.View;

public record MemberInfo(
        int generation,
        String memberName
) implements View {
}
