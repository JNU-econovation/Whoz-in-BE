package com.whoz_in.main_api.query.open_api.member.application.by_date;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface MembersByDateViewer extends Viewer {
    MembersOnDateView findAllByDate(LocalDate date);
    List<MembersOnDateView> findAllByYearMonth(YearMonth yearMonth);
}
