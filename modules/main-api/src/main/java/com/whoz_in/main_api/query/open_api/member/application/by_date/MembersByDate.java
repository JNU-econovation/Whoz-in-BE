package com.whoz_in.main_api.query.open_api.member.application.by_date;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record MembersByDate(
        List<MembersOnDateView> dates
) implements Response {
}
