package com.whoz_in.main_api.query.api.member.application.profile;

import com.whoz_in.main_api.query.shared.application.Query;
import java.util.UUID;

public record MemberProfileGet(
        UUID memberId
) implements Query {
}
