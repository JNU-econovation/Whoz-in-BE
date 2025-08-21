package com.whoz_in.main_api.query.member.application.block;

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
            throw new IllegalArgumentException("아직 %d년 %d월입니다.".formatted(thisMonth.getYear(), thisMonth.getMonthValue()));
        }
        return requested;
    }
}
