package com.whoz_in.main_api.query.api.member.application.block;

import com.whoz_in.main_api.query.api.member.exception.InvalidDateException;
import com.whoz_in.main_api.query.shared.application.Query;
import java.time.LocalDate;
import java.util.UUID;

public record MemberBlockGet(
    UUID memberId,
    LocalDate yearMonth
) implements Query {

    public MemberBlockGet(UUID memberId, int year, int month) {
        this(memberId, toYearMonth(year, month));
    }
    private static LocalDate toYearMonth(int year, int month){
        LocalDate today = LocalDate.now();
        LocalDate requested = LocalDate.of(year, month, 1);
        LocalDate thisMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        if (requested.isAfter(thisMonth)) {
            throw InvalidDateException.of(thisMonth.getYear(), thisMonth.getMonthValue());
        }
        return requested;
    }
}
