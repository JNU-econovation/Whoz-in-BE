package com.whoz_in.main_api.query.open_api.member.application.shared;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.UUID;

public record MemberInfoView(
        UUID memberId,
        int generation,
        String name
) implements View {

}
