package com.whoz_in.main_api.query.open_api.member.application.daily;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record DailyMembers(
        List<DailyMember> members,
        int size
) implements Response {
}
