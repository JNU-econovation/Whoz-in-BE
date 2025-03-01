package com.whoz_in.main_api.query.member.application.view;

import com.whoz_in.domain.member.model.Position;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;

public record MemberInfo(
        UUID memberId,
        String position,
        String statusMessage,
        int generation,
        String memberName
) implements View {
}
