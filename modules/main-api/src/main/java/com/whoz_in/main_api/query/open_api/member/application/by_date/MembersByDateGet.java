package com.whoz_in.main_api.query.open_api.member.application.by_date;

import com.whoz_in.main_api.query.open_api.member.exception.InvalidDateException;
import com.whoz_in.main_api.query.shared.application.Query;
import com.whoz_in.shared.DayBoundaryUtil;
import com.whoz_in.shared.Nullable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;

public record MembersByDateGet(
        int year,
        int month,
        @Nullable Integer day
) implements Query {

    public MembersByDateGet {
        LocalDate today = DayBoundaryUtil.today();
        if (day == null) {
            validateYearMonth(year, month, today);
        } else {
            validateDate(year, month, day, today);
        }
    }

    public boolean isSingleDate() {
        return day != null;
    }

    public LocalDate date() {
        if (!isSingleDate()) {
            throw new IllegalStateException("day is required for date()");
        }
        return LocalDate.of(year, month, day);
    }

    public YearMonth yearMonth() {
        return YearMonth.of(year, month);
    }

    private static void validateYearMonth(int year, int month, LocalDate today) {
        try {
            YearMonth requested = YearMonth.of(year, month);
            YearMonth current = YearMonth.from(today);
            if (requested.isAfter(current)) {
                throw InvalidDateException.futureMonth(current.getYear(), current.getMonthValue());
            }
        } catch (DateTimeException e) {
            throw InvalidDateException.invalid();
        }
    }

    private static void validateDate(int year, int month, int day, LocalDate today) {
        try {
            LocalDate requested = LocalDate.of(year, month, day);
            if (requested.isAfter(today)) {
                throw InvalidDateException.futureDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
            }
        } catch (DateTimeException e) {
            throw InvalidDateException.invalid();
        }
    }
}
