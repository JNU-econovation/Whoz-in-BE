package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public record ActivitiesView(
    List<Activity> days,
    Duration totalActiveTime
) implements View {
    public record Activity(LocalDate date, Duration activeTime) {}
}
